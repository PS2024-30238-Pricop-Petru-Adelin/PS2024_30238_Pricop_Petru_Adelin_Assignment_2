package com.olxapplication.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.olxapplication.entity.User;
@Service
public class RabbitMQSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private Queue queue;

    private static Logger logger = LogManager.getLogger(RabbitMQSender.class.toString());
    public void send(String string) {
        rabbitTemplate.convertAndSend(queue.getName(), string);
        logger.info("Sending Message to the Queue : " + string.toString());
    }
}
