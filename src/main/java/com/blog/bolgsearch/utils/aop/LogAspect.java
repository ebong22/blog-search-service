package com.blog.bolgsearch.utils.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class LogAspect {

    @Pointcut("execution(* com.kakaobanktest.bolgsearch.api..*.*(..))")
    private void api(){};
    @Pointcut("execution(* com.kakaobanktest.bolgsearch.utils..*.*(..))")
    private void utils(){};

    @Around("api() || utils()")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("Start [{} : {}] Arg={}", joinPoint.getTarget(), joinPoint.getSignature().getName(), joinPoint.getArgs());

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();
        log.info("End [{} : {}] RunningTime={}ms", joinPoint.getTarget(), joinPoint.getSignature().getName(), (end - start));

        return result;
    }
}
