package cl.duoc.rednorte.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "rednorte.events";
    public static final String AUDIT_QUEUE = "rednorte.audit";
    public static final String NOTIFICATION_QUEUE = "rednorte.notifications";
    public static final String AUDIT_ROUTING_KEY = "audit.events";

    @Bean
    TopicExchange redNorteExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue auditQueue() {
        return new Queue(AUDIT_QUEUE, true);
    }

    @Bean
    Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    Binding auditBinding(
            @Qualifier("auditQueue") Queue auditQueue,
            TopicExchange redNorteExchange) {
        return BindingBuilder.bind(auditQueue).to(redNorteExchange).with(AUDIT_ROUTING_KEY);
    }

    @Bean
    Binding notificationBinding(
            @Qualifier("notificationQueue") Queue notificationQueue,
            TopicExchange redNorteExchange) {
        return BindingBuilder.bind(notificationQueue).to(redNorteExchange).with(AUDIT_ROUTING_KEY);
    }

    @Bean
    Jackson2JsonMessageConverter rabbitMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
