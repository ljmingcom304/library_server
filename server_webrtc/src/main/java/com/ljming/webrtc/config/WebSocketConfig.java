package com.ljming.webrtc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Title:WebSocketConfig
 * <p>
 * Description:配置WebSocket支持
 * </p >
 * Author Jming.L
 * Date 2022/3/14 16:13
 */
@Configuration
public class WebSocketConfig extends WebMvcConfigurationSupport {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
