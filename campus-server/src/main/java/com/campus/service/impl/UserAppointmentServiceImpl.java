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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserAppointmentServiceImpl implements UserAppointmentService {
    @Autowired
    UserAppointmentMapper userAppointmentMapper;
    @Autowired
    ResourceMapper resourceMapper;
    @Autowired
    FacilityMapper facilityMapper;
    // 定义学校运营时间 (也可以写在配置文件里)
    private static final LocalTime OPEN_TIME = LocalTime.of(8, 0);  // 08:00
    private static final LocalTime CLOSE_TIME = LocalTime.of(22, 0); // 22:00
    private static final int STEP_MINUTES = 60; // 步长：1小时 (你可以改成30)
    @Override
    public List<TimeSlotVO> getAvailability(Long resourceId, LocalDate date, String type) {
        // 1. 查询该资源当天所有的【有效预约】(status 为 0 或 1 的)
        // 这一步是为了拿到 "BUSY" 的数据源
        List<Appointment> existAppts = userAppointmentMapper.findOccupied(resourceId,type,date);

        List<TimeSlotVO> resultList = new ArrayList<>();
        LocalTime currentSlotStart = OPEN_TIME;
        int index = 0;

        // 获取当前时刻，用于判断 "EXPIRED"
        LocalDateTime now = LocalDateTime.now();

        // 2. 循环生成时间槽
        while (currentSlotStart.isBefore(CLOSE_TIME)) {
            LocalTime currentSlotEnd = currentSlotStart.plusMinutes(STEP_MINUTES);

            // 防止超出闭馆时间
            if (currentSlotEnd.isAfter(CLOSE_TIME)) currentSlotEnd = CLOSE_TIME;

            // 默认状态
            String status = "FREE";
            String tips = "可预约";

            // 3. 判断是否过期 (EXPIRED)
            // 如果查询的是 "今天" 且 "槽开始时间" 早于 "当前时间"，那就是过期
            if (date.isEqual(LocalDate.now()) && currentSlotStart.isBefore(LocalTime.now())) {
                status = "EXPIRED";
                tips = "已过期";
            }
            // 如果查询的是 "过去的日子"，全天过期
            else if (date.isBefore(LocalDate.now())) {
                status = "EXPIRED";
                tips = "已过期";
            } else {
                // 4. 判断是否被占用 (BUSY)
                // 遍历数据库查出来的记录，看有没有撞期的
                for (Appointment dbAppt : existAppts) {
                    // 碰撞公式：(SlotStart < DbEnd) && (SlotEnd > DbStart)
                    if (currentSlotStart.isBefore(dbAppt.getEndTime()) &&
                            currentSlotEnd.isAfter(dbAppt.getStartTime())) {
                        status = "BUSY";
                        tips = "已被占用";
                        break; // 只要撞了一个，这个格子就废了，跳出循环
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
    @Transactional(rollbackFor = Exception.class)
    public void submitAppointment(AppointmentDTO dto) {
        // 1. 二次检查时间冲突 (防止两人同时点绿色格子)
        // 这里的 SQL 逻辑是：count > 0 表示有冲突
        Integer count = userAppointmentMapper.countConflict(
                dto.getResourceId(),
                dto.getAppointDate(),
                dto.getStartTime(),
                dto.getEndTime()
        );

        if (count > 0) {
            throw new RuntimeException("手慢了！该时间段刚刚已被抢占，请刷新后重试。");
        }

        // 2. 组装实体对象
        Appointment appointment = new Appointment();
        BeanUtils.copyProperties(dto, appointment);

        // 从拦截器 ThreadLocal 中获取当前学生ID
        appointment.setUserId(BaseContext.getCurrentId());
        appointment.setStatus(0); // 0: 待审核
        appointment.setCreateTime(LocalDateTime.now());
        appointment.setUpdateTime(LocalDateTime.now());

        // 3. 插入数据库
        userAppointmentMapper.insert(appointment);
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
