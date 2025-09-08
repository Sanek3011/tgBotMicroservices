package org.example.bff.kafka.producer.reportProducers;

import org.example.bff.kafka.events.reportEvents.ReportCreateEvent;
import org.example.bff.kafka.producer.AbstractProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReportCreateProducer extends AbstractProducer<ReportCreateEvent> {

    public static final String TOPIC = "report.create";

    public ReportCreateProducer(KafkaTemplate<String, ReportCreateEvent> kafkaTemplate) {
        super(kafkaTemplate, TOPIC);
    }

}
