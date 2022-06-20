package com.ljming.webrtc;


import com.ljming.webrtc.filter.HttpServletFilter;
import com.ljming.webrtc.project.controller.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;

import java.util.Arrays;


@ComponentScan({"com.ljming.webrtc"})
@SpringBootApplication
public class WebRTCApplication {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(WebRTCApplication.class);
        logger.info("[" + WebRTCApplication.class.getSimpleName() + "开始启动]" + Arrays.toString(args));
        ConfigurableApplicationContext context = SpringApplication.run(WebRTCApplication.class, args);
        WebSocketServer.setApplicationContext(context);
    }

    @Bean
    @Order(1)
    public FilterRegistrationBean<HttpServletFilter> httpServletRequestReplacedRegistration() {
        FilterRegistrationBean<HttpServletFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new HttpServletFilter());
        registration.addUrlPatterns("/*");
        registration.setName("httpServletFilter");
        return registration;
    }

}
