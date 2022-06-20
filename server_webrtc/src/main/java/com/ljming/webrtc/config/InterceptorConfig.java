package com.ljming.webrtc.config;


import com.ljming.webrtc.interceptor.HttpInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Title:InterceptorConfig
 * <p>
 * Description:拦截器配置
 * </p >
 * Author Jming.L
 * Date 2022/3/11 13:33
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpInterceptor()).addPathPatterns("/**");
    }


}
