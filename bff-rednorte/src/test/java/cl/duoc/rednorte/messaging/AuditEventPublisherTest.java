package cl.duoc.rednorte.messaging;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuditEventPublisherTest {

    @Test
    void publicaEventoEnExchange() {
        RabbitTemplate template = mock(RabbitTemplate.class);
        AuditEventPublisher publisher = new AuditEventPublisher(template, "rednorte.events", "audit.events");

        publisher.publish("PACIENTE_CREADO", "payload");

        verify(template).convertAndSend(eq("rednorte.events"), eq("audit.events"), isA(Object.class));
    }

    @Test
    void noInterrumpeOperacionSiRabbitNoEstaDisponible() {
        RabbitTemplate template = mock(RabbitTemplate.class);
        doThrow(new AmqpException("broker caido"))
                .when(template)
                .convertAndSend(eq("rednorte.events"), eq("audit.events"), isA(Object.class));
        AuditEventPublisher publisher = new AuditEventPublisher(template, "rednorte.events", "audit.events");

        assertDoesNotThrow(() -> publisher.publish("CITA_CREADA", "payload"));
    }
}
