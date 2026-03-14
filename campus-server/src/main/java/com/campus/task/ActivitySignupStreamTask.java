package com.campus.task;

import com.campus.entity.ActivitySignup;
import com.campus.mapper.ActivityMapper;
import com.campus.mapper.ActivitySignupMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ActivitySignupStreamTask {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivitySignupMapper activitySignupMapper;

    private static final String QUEUE_KEY = "queue.activity.signup";

    /**
     * 持续消费 Stream 消息 (这里用 @Scheduled 模拟轮询，实际生产建议用单独线程或 While 循环)
     * 每 500ms 拉取一次
     */
    @Scheduled(fixedDelay = 500)
    public void consumeStream() {
        try {
            String message = redisTemplate.opsForList().leftPop(QUEUE_KEY, 2, TimeUnit.SECONDS);
            if (message == null || message.isEmpty()) {
                return;
            }

            String[] parts = message.split(":", 3);
            if (parts.length < 2) {
                return;
            }
            Long userId = Long.valueOf(parts[0]);
            Long activityId = Long.valueOf(parts[1]);
            processSignup(userId, activityId);

        } catch (Exception e) {
            log.error("Stream consume error", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void processSignup(Long userId, Long activityId) {
        // 1. 幂等性检查：查库看是否已报名
        Integer count = activitySignupMapper.countByUserIdAndActivityId(userId, activityId);
        if (count > 0) {
            log.info("User {} already signed up activity {}, skip.", userId, activityId);
            return;
        }

        // 2. 插入报名记录
        ActivitySignup signup = ActivitySignup.builder()
                .activityId(activityId)
                .userId(userId)
                .signupTime(LocalDateTime.now())
                .build();
        activitySignupMapper.insert(signup);

        // 3. 增加活动当前人数
        activityMapper.increaseParticipants(activityId);
        
        log.info("User {} signup activity {} success (Async DB).", userId, activityId);
    }
}
