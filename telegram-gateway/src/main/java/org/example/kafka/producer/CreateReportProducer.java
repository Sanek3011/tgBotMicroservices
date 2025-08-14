package org.example.kafka.producer;

import lombok.RequiredArgsConstructor;

import org.example.kafka.events.OrderRequestedEvent;
import org.example.kafka.events.ReportCreateEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateReportProducer {

    private final KafkaTemplate<String, ReportCreateEvent> kafkaTemplate;

    public static final String TOPIC = "report.create";

    public void sendRequestOrder(ReportCreateEvent event) {
        kafkaTemplate.send(TOPIC, UUID.randomUUID().toString(), event);
    }
}
