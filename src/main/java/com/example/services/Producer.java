package com.example.services;

import com.example.JmsConfiguration;
import com.example.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;

@Component
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    @Autowired
    ConnectionFactory activeMqConnectionFactory;

    @Autowired
    MessageConverter messageConverter;

    public void sendMessage(Order order) {
        // You can create a bean of type JmsTemplate
        JmsTemplate jmsTemplate = new JmsTemplate(activeMqConnectionFactory);
        jmsTemplate.setMessageConverter(messageConverter);
        jmsTemplate.convertAndSend(JmsConfiguration.QUEUE_NAME, order);
        logger.info("[Producer] Message was sent");
    }
}
