package com.rednorte.servicio_auditoria.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "rednorte.events";
    public static final String QUEUE = "rednorte.audit";
    public static final String ROUTING_KEY = "audit.events";

    @Bean
    TopicExchange redNorteExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue auditQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    Binding auditBinding(Queue auditQueue, TopicExchange redNorteExchange) {
        return BindingBuilder.bind(auditQueue).to(redNorteExchange).with(ROUTING_KEY);
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
