package com.ljming.consumer.project.controller;

import com.ljming.consumer.project.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Title:DubboController
 * <p>
 * Description:
 * </p >
 * Author Jming.L
 * Date 2022/4/1 9:57
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController {


    @Autowired
    ConsumerService consumerService;

    @RequestMapping("/select")
    public String selectMessage(String message) {
        consumerService.select(message);
        return "select message:" + message;
    }

    @RequestMapping("/insert")
    public String insertMessage(String message) {
        consumerService.insert(message);
        return "select message:" + message;
    }

}
