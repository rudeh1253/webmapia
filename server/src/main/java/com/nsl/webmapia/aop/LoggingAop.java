package com.nsl.webmapia.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAop {

    @Pointcut("execution(public * com.nsl.webmapia..controller..*(..))")
    private void forControllers() {}

    @Before("forControllers()")
    public void logArgValues(JoinPoint joinPoint) {
        System.out.println(joinPoint.getSignature());
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        String[] paramNames = methodSignature.getParameterNames();
        Class[] paramTypes = methodSignature.getParameterTypes();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < paramNames.length; i++) {
            System.out.println(String.format("[%s %s]: %s", paramTypes[i].toString(), paramNames[i], args[i].toString()));
        }
        System.out.println();
    }
}
