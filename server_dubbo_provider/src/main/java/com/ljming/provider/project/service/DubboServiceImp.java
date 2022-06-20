package com.ljming.provider.project.service;

import com.ljming.service.DubboService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Title:DubboServiceImp
 * <p>
 * Description:
 * </p >
 * Author Jming.L
 * Date 2022/3/31 17:34
 */
@Service
@com.alibaba.dubbo.config.annotation.Service(interfaceClass = DubboService.class)
public class DubboServiceImp implements DubboService {


    private final static Logger logger = Logger.getLogger(DubboServiceImp.class);

    @Override
    public void select(String message) {
        logger.info("提供服务============》查询:" + message);
    }

    @Override
    public void insert(String message) {
        logger.info("提供服务============》插入" + message);
    }

}
