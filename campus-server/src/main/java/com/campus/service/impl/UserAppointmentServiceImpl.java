package com.campus.service.impl;
import com.campus.context.BaseContext;
import com.campus.dto.AppointmentDTO;
import com.campus.dto.FacilityPageQueryDto;
import com.campus.dto.ResourcePageQueryDTO;
import com.campus.entity.Appointment;
import com.campus.entity.Facility;
import com.campus.entity.Resource;
import com.campus.mapper.FacilityMapper;
import com.campus.mapper.ResourceMapper;
import com.campus.mapper.UserAppointmentMapper;
import com.campus.service.UserAppointmentService;
import com.campus.vo.FacilityVO;
import com.campus.vo.ResourceVO;
import com.campus.vo.TimeSlotVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
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
    private static final String REDIS_KEY_RESOURCE_AVAILABLE = "campus:resource:available";
    private static final String REDIS_KEY_FACILITY_AVAILABLE = "campus:facility:available";
    private static final String REDIS_KEY_NULL_CACHE_PREFIX = "campus:null:cache:";
    // 布隆过滤器Key
    private static final String BLOOM_FILTER_RESOURCE = "bloom:resource";
    private static final String BLOOM_FILTER_FACILITY = "bloom:facility";

    private RBloomFilter<Long> resourceBloomFilter;
    private RBloomFilter<Long> facilityBloomFilter;

    @PostConstruct
    public void init() {
        // 初始化布隆过滤器
        resourceBloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER_RESOURCE);
        // 初始化预期插入量为 10000，误判率为 0.03
        resourceBloomFilter.tryInit(10000L, 0.03);
        facilityBloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER_FACILITY);
        facilityBloomFilter.tryInit(10000L, 0.03);
        // 启动时预加载一次
        preprocessResources("RESOURCE");
        preprocessResources("VENUE");
    }

    /**
     * 预热可用资源到 Redis 和布隆过滤器
     * @param type 资源类型 RESOURCE 或 VENUE
     */
    private void preprocessResources(String type) {
        if ("RESOURCE".equals(type)) {
            ResourcePageQueryDTO queryDTO = new ResourcePageQueryDTO();
            queryDTO.setStatus(1); // 1: 可借
            // 查询所有可用的资源
            List<ResourceVO> list = resourceMapper.pageList(queryDTO);
            if (list != null && !list.isEmpty()) {
                String[] ids = list.stream().map(r -> r.getId().toString()).toArray(String[]::new);
                // 更新 Redis Set
                redisTemplate.delete(REDIS_KEY_RESOURCE_AVAILABLE);
                redisTemplate.opsForSet().add(REDIS_KEY_RESOURCE_AVAILABLE, ids);
                redisTemplate.expire(REDIS_KEY_RESOURCE_AVAILABLE, 1, TimeUnit.HOURS);
                
                // 更新布隆过滤器
                for (ResourceVO vo : list) {
                    resourceBloomFilter.add(vo.getId());
                }
            }
        } else if ("VENUE".equals(type)) {
            FacilityPageQueryDto queryDTO = new FacilityPageQueryDto();
            queryDTO.setStatus(1); // 1: 可用
            // 查询所有可用的场所
            List<FacilityVO> list = facilityMapper.pageList(queryDTO);
            if (list != null && !list.isEmpty()) {
                String[] ids = list.stream().map(f -> f.getId().toString()).toArray(String[]::new);
                redisTemplate.delete(REDIS_KEY_FACILITY_AVAILABLE);
                redisTemplate.opsForSet().add(REDIS_KEY_FACILITY_AVAILABLE, ids);
                redisTemplate.expire(REDIS_KEY_FACILITY_AVAILABLE, 1, TimeUnit.HOURS);
                
                // 更新布隆过滤器
                for (FacilityVO vo : list) {
                    facilityBloomFilter.add(vo.getId());
                }
            }
        }
    }
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
        // 缓存穿透处理：检查资源是否在可用列表中
        String type = dto.getType();
        Long resourceId = dto.getResourceId();
        String redisKey = null;
        RBloomFilter<Long> bloomFilter = null;
        String nullCacheKey = REDIS_KEY_NULL_CACHE_PREFIX + type + ":" + resourceId;
        if ("RESOURCE".equals(type)) {
            redisKey = REDIS_KEY_RESOURCE_AVAILABLE;
            bloomFilter = resourceBloomFilter;
        } else if ("VENUE".equals(type)) {
            redisKey = REDIS_KEY_FACILITY_AVAILABLE;
            bloomFilter = facilityBloomFilter;
        }

        if (redisKey != null && bloomFilter != null) {
            // 0. 先检查空对象缓存（针对布隆误判或已确认不存在的ID）
            if (Boolean.TRUE.equals(redisTemplate.hasKey(nullCacheKey))) {
                throw new RuntimeException("该资源不存在或已下架");
            }

            // 1. 布隆过滤器拦截（高效拦截绝大多数不存在的ID）
            if (!bloomFilter.contains(resourceId)) {
                throw new RuntimeException("该资源不存在或已下架");
            }

            // 2. 检查 Redis 白名单
            String idStr = resourceId.toString();
            Boolean exists = redisTemplate.opsForSet().isMember(redisKey, idStr);
            
            if (Boolean.FALSE.equals(exists)) {
                // 3. 如果布隆说有，但 Redis 说没有 -> 可能是 Redis 缓存过期，也可能是布隆误判
                // 尝试预热（刷新 Redis Set）
                preprocessResources(type);
                
                // 4. 再次检查 Redis
                exists = redisTemplate.opsForSet().isMember(redisKey, idStr);
                
                // 5. 如果还是没有 -> 说明是布隆误判，或者是刚下架的资源
                if (Boolean.FALSE.equals(exists)) {
                    // 缓存空对象，防止短期内再次触发预热
                    redisTemplate.opsForValue().set(nullCacheKey, "", 5, TimeUnit.MINUTES);
                    throw new RuntimeException("该资源不存在或已下架");
                }
            }
        }

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
