package com.ljming.webrtc.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;


/**
 * Title:MyBatisConfig
 * <p>
 * Description:MyBatis配置
 * </p >
 * Author Jming.L
 * Date 2022/3/14 10:04
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.ljming.webrtc", sqlSessionFactoryRef = "sqlSessionFactory")
public class MyBatisConfig {


    @Autowired
    @Qualifier("webrtcdb")  //DataSourceConfig下实现该类，@Autowired与@Qualifier配合使用
    private DataSource dataSource;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        //给包中类注册别名
        factoryBean.setTypeAliasesPackage("com.ljming.webrtc.bean");
        factoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:/mapper/*.xml");
        factoryBean.setMapperLocations(resources);

        return factoryBean.getObject();
    }

}
