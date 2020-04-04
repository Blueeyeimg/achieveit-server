package com.ecnu.achieveit.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.ecnu.achieveit.util.LogUtil.i;

@Aspect
@Component
@Order(100)
public class WebLogAspect {

    /**
     * 横切点
     */
    @Pointcut("execution(public * com.ecnu.achieveit.controller..*.*(..))")
    public void webLog() {
    }
    /**
     * 接收请求，并记录数据
     * @param joinPoint
     */
    @Before(value = "webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 接收到请求
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        // 记录请求内容，threadInfo存储所有内容
        i("URL : " + request.getRequestURL());

        i("URI : " + request.getRequestURI());

        i("HTTP_METHOD : " + request.getMethod());

        i("REMOTE_ADDR : " + request.getRemoteAddr());

        i("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName());

        i("ARGS : " + Arrays.toString(joinPoint.getArgs()));

        i("USER_AGENT"+request.getHeader("User-Agent"));

    }

    /**
     * 获取执行时间
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object ob = proceedingJoinPoint.proceed();
        Long takeTime = System.currentTimeMillis() - startTime;
        i("耗时：" + takeTime);
        return ob;
    }

}