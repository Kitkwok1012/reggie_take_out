package org.learn.reggie.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class LogAspect {

    private final static Logger logger         = LoggerFactory.getLogger(LogAspect.class);
    @Pointcut("@annotation(org.learn.reggie.log.WebLog)")
    public void webLog() {}


    @Before("webLog()")
    public void before(JoinPoint joinPoint) {
        logger.info("===== before advice starts =====");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logger.info("request URL   : {}", request.getRequestURI());
        logger.info("HTTP Method   : {}", request.getMethod());
        logger.info("IP            : {}", request.getRemoteAddr());
        logger.info("===== before advice ends =====");
    }

    @After("webLog()")
    public void after(JoinPoint joinPoint) {

    }


}
