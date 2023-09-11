package com.nsl.webmapia.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAop {

    @Pointcut("execution(public * com.nsl.webmapia..controller..*(..))")
    private void forControllers() {}

    @Pointcut("execution(public * com.nsl.webmapia..service..*(..))")
    private void forServices() {}

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

    @AfterReturning(pointcut = "forServices()", returning = "result")
    public void logReturnValuesOfServices(JoinPoint joinPoint, Object result) {
        System.out.println(joinPoint.getSignature());
        System.out.println("Returns: " + result);
    }
}
