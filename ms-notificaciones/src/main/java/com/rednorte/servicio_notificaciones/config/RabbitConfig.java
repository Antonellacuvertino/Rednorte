package com.rednorte.servicio_notificaciones.config;

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
    public static final String QUEUE = "rednorte.notifications";
    public static final String ROUTING_KEY = "audit.events";

    @Bean
    TopicExchange redNorteExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue notificationQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    Binding notificationBinding(Queue notificationQueue, TopicExchange redNorteExchange) {
        return BindingBuilder.bind(notificationQueue).to(redNorteExchange).with(ROUTING_KEY);
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
