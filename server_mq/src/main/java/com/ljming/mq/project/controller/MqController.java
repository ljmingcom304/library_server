package com.ljming.mq.project.controller;

import com.ljming.mq.project.service.MqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Title:MqController
 * <p>
 * Description:消息队列
 * </p >
 * Author Jming.L
 * Date 2022/3/31 14:02
 */
@RestController
@RequestMapping("/mq")
public class MqController {

    @Autowired
    MqService mqService;

    @RequestMapping("/send")
    public String sendMessage(String message) {
        mqService.sendMessage(message);
        return "send message:" + message;
    }

}
