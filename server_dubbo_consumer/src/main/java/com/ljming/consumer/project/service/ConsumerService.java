package com.ljming.consumer.project.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ljming.service.DubboService;
import org.springframework.stereotype.Service;

/**
 * Title:ConsumerService
 * <p>
 * Description:
 * </p >
 * Author Jming.L
 * Date 2022/4/12 10:47
 */
@Service
public class ConsumerService {

    @Reference
    DubboService dubboService;

    public void select(String message) {
        dubboService.select(message);
    }


    public void insert(String message) {
        dubboService.insert(message);
    }

}
