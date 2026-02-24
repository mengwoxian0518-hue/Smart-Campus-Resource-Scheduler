package com.campus.AOP;

import com.alibaba.fastjson.JSON;
import com.campus.annotation.Log;
import com.campus.context.BaseContext;
import com.campus.entity.SysLog;
import com.campus.mapper.SysLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private SysLogMapper sysLogMapper;
    @Around("@annotation(com.campus.annotation.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - beginTime;
        try {
            saveLog(joinPoint, executionTime);
        } catch (Exception e) {
            log.error("保存日志失败: {}", e.getMessage());
        }

        return result;
    }
    //todo 可以用aysnc异步处理不阻塞任务进行
    private void saveLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        SysLog sysLog = SysLog.builder()
                .module(logAnnotation.module())
                .action(logAnnotation.action())
                .methodName(signature.getDeclaringTypeName() + "." + method.getName())
                .params(JSON.toJSONString(joinPoint.getArgs()))
                .ip(request.getRemoteAddr())
                .operatorId(BaseContext.getCurrentId())
                .executionTime(time)
                .createTime(LocalDateTime.now())
                .build();

        // 存入数据库
        sysLogMapper.insert(sysLog);
    }
}