package com.campus.service.impl;

import com.campus.context.BaseContext;
import com.campus.dto.AppointmentDTO;
import com.campus.entity.Appointment;
import com.campus.entity.Facility;
import com.campus.entity.Resource;
import com.campus.mapper.FacilityMapper;
import com.campus.mapper.ResourceMapper;
import com.campus.mapper.UserAppointmentMapper;
import com.campus.service.UserAppointmentService;
import com.campus.vo.TimeSlotVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserAppointmentServiceImpl implements UserAppointmentService {
    @Autowired
    UserAppointmentMapper userAppointmentMapper;
    @Autowired
    ResourceMapper resourceMapper;
    @Autowired
    FacilityMapper facilityMapper;
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    StringRedisTemplate redisTemplate;
    private static final LocalTime OPEN_TIME = LocalTime.of(8, 0);
    private static final LocalTime CLOSE_TIME = LocalTime.of(22, 0);
    private static final int STEP_MINUTES = 60;
    @Override
    public List<TimeSlotVO> getAvailability(Long resourceId, LocalDate date, String type) {
        List<Appointment> existAppts = userAppointmentMapper.findOccupied(resourceId,type,date);

        List<TimeSlotVO> resultList = new ArrayList<>();
        LocalTime currentSlotStart = OPEN_TIME;
        int index = 0;
        LocalDateTime now = LocalDateTime.now();

        while (currentSlotStart.isBefore(CLOSE_TIME)) {
            LocalTime currentSlotEnd = currentSlotStart.plusMinutes(STEP_MINUTES);


            if (currentSlotEnd.isAfter(CLOSE_TIME)) currentSlotEnd = CLOSE_TIME;

            String status = "FREE";
            String tips = "可预约";

            if (date.isEqual(LocalDate.now()) && currentSlotStart.isBefore(LocalTime.now())) {
                status = "EXPIRED";
                tips = "已过期";
            }

            else if (date.isBefore(LocalDate.now())) {
                status = "EXPIRED";
                tips = "已过期";
            } else {

                for (Appointment dbAppt : existAppts) {
                    if (currentSlotStart.isBefore(dbAppt.getEndTime()) &&
                            currentSlotEnd.isAfter(dbAppt.getStartTime())) {
                        status = "BUSY";
                        tips = "已被占用";
                        break;
                    }
                }
            }

            // 5. 封装 VO
            TimeSlotVO vo = TimeSlotVO.builder()
                    .index(index++)
                    .timeDisplay(currentSlotStart.toString())
                    .startTime(currentSlotStart)
                    .endTime(currentSlotEnd)
                    .status(status)
                    .tips(tips)
                    .build();
            resultList.add(vo);
            currentSlotStart = currentSlotEnd;
        }

        return resultList;
    }
    @Override
    public void submitAppointment(AppointmentDTO dto) {
        //二次检查时间冲突
//        synchronized (this) {
        String lockKey="lock:appointment:"+dto.getResourceId()+":"+dto.getAppointDate()+":"+dto.getStartTime()+":"+dto.getEndTime();
        RLock lock = redissonClient.getLock(lockKey);
        try{
            if(lock.tryLock(5,10, TimeUnit.SECONDS))
            {
                try
                {
                    Integer count = userAppointmentMapper.countConflict(
                            dto.getResourceId(),
                            dto.getAppointDate(),
                            dto.getStartTime(),
                            dto.getEndTime()
                    );
                    if (count > 0) {
                        throw new RuntimeException("手慢了！该时间段刚刚已被抢占，请刷新后重试。");
                    }
                    Appointment appointment = new Appointment();
                    BeanUtils.copyProperties(dto, appointment);
                    appointment.setUserId(BaseContext.getCurrentId());
                    appointment.setStatus(0);
                    appointment.setCreateTime(LocalDateTime.now());
                    appointment.setUpdateTime(LocalDateTime.now());
                    userAppointmentMapper.insert(appointment);
                }finally {
                    lock.unlock();
                }
            }else {
                throw new RuntimeException("系统繁忙，请稍后再试");
            }
        }catch(InterruptedException e)
        {
            throw new RuntimeException("系统繁忙，请稍后再试");
        }
//        }
    }

    @Override
    public List<Appointment> list(String type, Integer status) {
        List<Appointment> appointments=new ArrayList<>();
        if(type.equals("RESOURCE"))
        {
            appointments = userAppointmentMapper.listResource(BaseContext.getCurrentId(), status);
            for (Appointment appointment : appointments) {
                Resource r = resourceMapper.getById(appointment.getResourceId());
                appointment.setResourceName(r.getName());
                appointment.setResourceImage(r.getImage());
            }
        }else {
            appointments = userAppointmentMapper.listFacility(BaseContext.getCurrentId(), status);
            for (Appointment appointment : appointments) {
                Facility r = facilityMapper.getById(appointment.getResourceId());
                appointment.setResourceName(r.getName());
            }
        }
        return appointments;
    }

    @Override
    public void cancel(Appointment dto) {
        userAppointmentMapper.cancel(dto);
    }

}
