package com.ljming.mq.project.service;

import com.ljming.mq.producer.MqProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Title:MqService
 * <p>
 * Description:
 * </p >
 * Author Jming.L
 * Date 2022/3/31 14:42
 */
@Service
public class MqService {

    @Autowired
    MqProducer mqProducer;

    public void sendMessage(String message) {
        mqProducer.sendMessage("push_message", message);
    }

}
