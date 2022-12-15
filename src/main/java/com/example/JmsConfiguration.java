package com.example;

import com.example.services.Consumer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

@Configuration
public class JmsConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    public static final String BROKER_URL = "tcp://localhost:61616";

    public static final String QUEUE_NAME = "test_spring_queue";

    @Bean
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(BROKER_URL);
        // Custom redelivery policy with no cap for redeliveries
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(RedeliveryPolicy.NO_MAXIMUM_REDELIVERIES);
        activeMQConnectionFactory.setRedeliveryPolicy(redeliveryPolicy);
        return activeMQConnectionFactory;
    }

    // Ability to set error handler - if @JmsListener methods will throw exceptions
    // But in case of exception occurred and the session working in acknowledge
    // modes AUTO_ACKNOWLEDGE or DUPS_OK_ACKNOWLEDGE -> messages still will be marked as received
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // Custom acknowledge mode: guaranteed redelivery in case if exception in consumer listener or JVM dye
        // more on this: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jms/listener/AbstractMessageListenerContainer.html
        factory.setSessionTransacted(true);
        // Other acknowledge modes can be set via: factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setErrorHandler(ex -> logger.error("JMS exception occurred!!!", ex));
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
