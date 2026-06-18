package cl.duoc.rednorte.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class AuditEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AuditEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public AuditEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${rednorte.rabbit.exchange:rednorte.events}") String exchange,
            @Value("${rednorte.rabbit.routing-key:audit.events}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void publish(String eventType, Object payload) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, Map.of(
                    "eventType", eventType,
                    "occurredAt", Instant.now().toString(),
                    "payload", payload));
        } catch (AmqpException ex) {
            log.warn("No se pudo publicar evento {} en RabbitMQ: {}", eventType, ex.getMessage());
        }
    }
}
