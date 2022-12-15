package com.example.services;

import com.example.JmsConfiguration;
import com.example.model.Order;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;

@Component
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    @Autowired
    ConnectionFactory activeMqConnectionFactory;

    @Autowired
    MessageConverter messageConverter;

    public void sendMessage(Order order) {
        JmsTemplate jmsTemplate = jmsTemplate();
        Destination destination = new ActiveMQQueue(JmsConfiguration.QUEUE_NAME);
        jmsTemplate.convertAndSend(destination, order);
        logger.info("[Producer] Message was sent");
    }

    // You can create a bean of JmsTemplate type
    private JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate(activeMqConnectionFactory);
        jmsTemplate.setMessageConverter(messageConverter);
        // Turn on ability to specify deliveryMode, priority, and timeToLive in JmsTemplate object
        jmsTemplate.setExplicitQosEnabled(true);
        logger.info("[Producer] JmsTemplate explicit QOS is set to: {}", jmsTemplate.isExplicitQosEnabled());
        // Specify deliveryMode
        // Another way: jmsTemplate.setDeliveryPersistent(false);
        jmsTemplate.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        return jmsTemplate;
    }
}
