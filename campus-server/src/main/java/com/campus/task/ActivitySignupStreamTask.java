package com.campus.task;

import com.campus.config.ActivitySignupRabbitConfig;
import com.campus.entity.ActivitySignup;
import com.campus.mapper.ActivityMapper;
import com.campus.mapper.ActivitySignupMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Slf4j
public class ActivitySignupStreamTask {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivitySignupMapper activitySignupMapper;

    @RabbitListener(queues = ActivitySignupRabbitConfig.ACTIVITY_SIGNUP_QUEUE)
    public void consumeSignupMessage(String message) {
        try {
            if (message == null || message.isEmpty()) {
                return;
            }
            String[] parts = message.split(":", 3);
            if (parts.length < 2) {
                log.warn("Invalid signup message: {}", message);
                return;
            }
            Long userId = Long.valueOf(parts[0]);
            Long activityId = Long.valueOf(parts[1]);
            processSignup(userId, activityId);
        } catch (Exception e) {
            log.error("RabbitMQ consume signup message error, payload={}", message, e);
            throw e;
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
