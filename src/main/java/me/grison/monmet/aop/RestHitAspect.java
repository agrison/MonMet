package me.grison.monmet.aop;

import me.grison.monmet.repository.AppRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RestHitAspect {
    @Autowired
    AppRepository appRepository;

    @AfterReturning("execution(* me.grison.monmet.rest.AppController.*(..))")
    public void logServiceAccess(JoinPoint joinPoint) {
        appRepository.incrementHits();
    }
}
