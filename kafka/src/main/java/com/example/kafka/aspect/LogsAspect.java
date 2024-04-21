package com.example.kafka.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogsAspect {
    private static final Logger LOGGER = LogManager.getLogger(LogsAspect.class);


    @Before("postPointCut() || getPointCut() ||putPointCut() ||deletePointCut()")
    public void afterAdvice(JoinPoint joinPoint)  {
        joinPoint.getSignature().getName();
        String methodName=joinPoint.getSignature().getName();
        LOGGER.info(" {} method  was called",methodName);


    }



    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postPointCut() {
        //do nothing
    }


    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getPointCut() {
        //do nothing
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putPointCut() {
        //do nothing
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deletePointCut() {
        //do nothing
    }
}
