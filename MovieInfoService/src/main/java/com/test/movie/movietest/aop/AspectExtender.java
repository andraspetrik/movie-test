package com.test.movie.movietest.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class AspectExtender {

    private static final Logger log = LoggerFactory.getLogger(AspectExtender.class);

    @Around("@annotation(com.test.movie.movietest.aop.Logged)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {

        // TODO Delete this
//        var signature = (MethodSignature) joinPoint.getSignature();
//        var method = signature.getMethod();
//        var annotation = (Logged) method.getAnnotation(Logged.class);

        var methodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();

        log.debug("[@Logged] {}", methodName);

        Object result = joinPoint.proceed();

        return result;
    }

    @Around("@annotation(com.test.movie.movietest.aop.LoggedExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        var stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed();

        stopWatch.stop();

        log.debug("[@LoggedExecutionTime] {} executed in {} ms", joinPoint.getSignature(), stopWatch.getTotalTimeMillis());

        return result;
    }

}
