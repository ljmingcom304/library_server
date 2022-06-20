package com.ljming.file.project.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

/**
 * Title:IndexSwaggerConfig
 * <p>
 * Description:
 * </p >
 * Author Jming.L
 * Date 2022/4/13 11:53
 */
@Configuration
public class MultipartConfig {

    /**
     * 配置文件上传大小
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        DataSize dataSize = DataSize.ofMegabytes(10);
        // 单个数据大小 10M
        factory.setMaxFileSize(dataSize);
        /// 总上传数据大小 10M
        factory.setMaxRequestSize(dataSize);
        return factory.createMultipartConfig();
    }

}