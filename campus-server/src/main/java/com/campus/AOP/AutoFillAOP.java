package com.campus.AOP;

import com.campus.Type.OperationType;
import com.campus.annotation.AutoFill;
import com.campus.context.BaseContext;
import com.campus.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
@Component
@Aspect
public class AutoFillAOP {
    @Pointcut("execution(* com.campus.mapper.*.*(..)) && @annotation(com.campus.annotation.AutoFill)")
    public void autoFill(){}
    @Before("autoFill()")
    public void before(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("开始进行数据填充");
        MethodSignature signature =(MethodSignature) joinPoint.getSignature();
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType value = annotation.value();
        Object[] args = joinPoint.getArgs();
        if(args==null||args.length==0)
        {
            return;
        }
        Object entity = args[0];
        Method setCreateTime = entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
        Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
        Method setCreateUser = entity.getClass().getDeclaredMethod("setCreateUser", Long.class);
        Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
        if(value==OperationType.INSERT)
        {
            setCreateUser.invoke(entity, BaseContext.getCurrentId());
            setUpdateTime.invoke(entity,LocalDateTime.now());
            setUpdateUser.invoke(entity,BaseContext.getCurrentId());
            setCreateTime.invoke(entity,LocalDateTime.now());
        }
        else if(value==OperationType.UPDATE)
        {
            setUpdateUser.invoke(entity,BaseContext.getCurrentId());
            setUpdateTime.invoke(entity,LocalDateTime.now());
        }
    }
}
