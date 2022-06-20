package com.ljming.mq.project.service;

import org.apache.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * Title:MsgService
 * <p>
 * Description:用于消息接收
 * </p >
 * Author Jming.L
 * Date 2022/3/31 14:56
 */
@Service
public class MsgService {

    private final static Logger logger = Logger.getLogger(MsgService.class);

    @JmsListener(destination = "push_message")
    public void handleMessage(String message) {
        logger.info("接收消息：" + message);
    }

}
