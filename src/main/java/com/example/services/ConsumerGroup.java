package com.example.services;

import com.example.JmsConfiguration;
import com.example.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Creates one {@link JmsListener} per each DefaultJmsListenerContainerFactory declared in {@link JmsConfiguration}.
 * Each {@link JmsListener} comes with unique client id.
 */
@Component
public class ConsumerGroup {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerGroup.class);

    @JmsListener(destination = JmsConfiguration.DESTINATION_NAME, containerFactory = "jmsListenerContainerFactory1")
    public void consumer1(Order order) {
        logger.info("[Consumer1] Received message: {}", order);
    }

    @JmsListener(destination = JmsConfiguration.DESTINATION_NAME, containerFactory = "jmsListenerContainerFactory2")
    public void consumer2(Order order) {
        logger.info("[Consumer2] Received message: {}", order);
    }
}
