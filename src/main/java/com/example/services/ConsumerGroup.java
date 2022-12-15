package com.example.services;

import com.example.JmsConfiguration;
import com.example.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerGroup {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerGroup.class);

    @JmsListener(destination = JmsConfiguration.DESTINATION_NAME)
    public void consumer1(Order order) {
        logger.info("[Consumer1] Received message: {}", order);
    }

    @JmsListener(destination = JmsConfiguration.DESTINATION_NAME)
    public void consumer2(Order order) {
        logger.info("[Consumer2] Received message: {}", order);
    }
}
