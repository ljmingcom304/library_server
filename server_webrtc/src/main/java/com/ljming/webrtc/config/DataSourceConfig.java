package com.ljming.webrtc.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Title:DataSourceConfig
 * <p>
 * Description:数据源配置
 * </p >
 * Author Jming.L
 * Date 2022/3/14 14:57
 */
@Configuration
public class DataSourceConfig {

    /**
     * 读取application-*.yml下的数据源配置
     */
    @Bean(name = "webrtcdb")
    @ConfigurationProperties(prefix = "spring.datasource.webrtcdb")
    public DataSource dataSource(){
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

}
