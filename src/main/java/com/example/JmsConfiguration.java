package com.example;

import com.example.services.ConsumerGroup;
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

    private static final Logger logger = LoggerFactory.getLogger(ConsumerGroup.class);

    public static final String BROKER_URL = "tcp://localhost:61616";

    public static final String DESTINATION_NAME = "spring_activemq_test";

    public static final Destinations DESTINATION_TYPE = Destinations.QUEUE;

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

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory1(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        return createJmsContainerFactory("consumer1", connectionFactory, messageConverter);
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory2(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        return createJmsContainerFactory("consumer2", connectionFactory, messageConverter);
    }

    private DefaultJmsListenerContainerFactory createJmsContainerFactory(String clientId, ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setClientId(clientId);
        // Custom acknowledge mode: guaranteed redelivery in case if exception in consumer listener or JVM dye
        // more on this: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jms/listener/AbstractMessageListenerContainer.html
        // Other acknowledge modes can be set via: factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setSessionTransacted(true);
        // Setting error handler - if @JmsListener methods will throw exceptions
        // But in case of exception occurred and the session working in acknowledge
        // modes AUTO_ACKNOWLEDGE or DUPS_OK_ACKNOWLEDGE -> messages still will be marked as received
        factory.setErrorHandler(ex -> logger.error("JMS exception occurred!!!", ex));
        factory.setMessageConverter(messageConverter);
        // PubSub means Publishers/Subscribers
        if (DESTINATION_TYPE == Destinations.TOPIC || DESTINATION_TYPE == Destinations.DURABLE_CONSUMERS_TOPIC) {
            factory.setPubSubDomain(true);
        }
        // If you want to consumer received all messages it missed during it's shutdown
        if (DESTINATION_TYPE == Destinations.DURABLE_CONSUMERS_TOPIC) {
            factory.setSubscriptionDurable(true);
        }
        return factory;
    }

    public enum Destinations {
        /**
         * JMS Queue destination
         */
        QUEUE,

        /**
         * JMS Topic destination
         */
        TOPIC,

        /**
         *  consumers are going to read all messages from topic,
         *  which may be missed by consumers during shutdown
         */
        DURABLE_CONSUMERS_TOPIC
    }
}
