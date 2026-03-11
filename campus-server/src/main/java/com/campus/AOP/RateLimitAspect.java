package com.campus.AOP;

import com.campus.annotation.RateLimit;
import com.campus.context.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

@Slf4j
@Aspect
@Component
public class RateLimitAspect {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private DefaultRedisScript<Long> redisScript;

    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/limit.lua")));
    }

    @Around("@annotation(com.campus.annotation.RateLimit)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        if (rateLimit != null) {
            String key = rateLimit.key();
            int time = rateLimit.time();
            int count = rateLimit.count();

            // 组合 Key: rate_limit:方法名:用户ID
            String combineKey = key + ":" + method.getName() + ":" + BaseContext.getCurrentId();
            List<String> keys = Collections.singletonList(combineKey);
            
            // 执行 Lua 脚本
            Long number = stringRedisTemplate.execute(redisScript, keys, String.valueOf(count), String.valueOf(time));

            if (number != null && number == 0) {
                // 超过限流
                throw new RuntimeException("访问过于频繁，请稍后再试");
            }
            log.info("限制请求'{}',当前请求'{}',缓存key'{}'", count, number.intValue(), key);
        }

        return pjp.proceed();
    }
}
