package com.ljming.webrtc.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Title:HttpInterceptor
 * <p>
 * Description:拦截器
 * </p >
 * Author Jming.L
 * Date 2022/3/11 13:31
 */
public class HttpInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("拦截器--------------->" + request.getRequestURI());
        return true;
    }

}
