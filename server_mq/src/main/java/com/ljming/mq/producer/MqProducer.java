package com.ljming.mq.producer;

import com.ljming.mq.project.service.MsgService;
import org.apache.activemq.broker.region.Destination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Title:MqProducer
 * <p>
 * Description:消息队列
 * </p >
 * Author Jming.L
 * Date 2022/3/31 12:59
 */
@Service
public class MqProducer {

    private final static Logger logger = Logger.getLogger(MsgService.class);
    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 发送文本消息
     */
    public void sendMessage(String destinationName, String body) {
        logger.info("发送消息：" + body);
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(destinationName);
        jmsTemplate.send(activeMQQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(body);
            }
        });
    }

    /**
     * 发送对象消息
     */
    public void sendMessage(String destinationName, Object body) {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(destinationName);
        jmsTemplate.convertAndSend(activeMQQueue, body);
    }

}
