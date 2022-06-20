package com.ljming.file.project.config;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wzy
 * @date 2021/6/8
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.minio")
public class MinioConfig {

    //地址
    private String endpoint;
    //AK
    private String accessKey;
    //SK
    private String secretKey;

    @Bean
    public MinioClient getMinioClient() throws InvalidEndpointException, InvalidPortException {
        return new MinioClient(endpoint, accessKey, secretKey);
    }

}
