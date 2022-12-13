package com.example.services;

import com.example.JmsConfiguration;
import com.example.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @JmsListener(destination = JmsConfiguration.QUEUE_NAME)
    public void receiveMessage(Order order) {
        logger.info("[Consumer] Received message: {}", order);
    }
}
