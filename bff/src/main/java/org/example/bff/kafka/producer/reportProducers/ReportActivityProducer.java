package org.example.bff.kafka.producer.reportProducers;

import org.example.bff.kafka.events.ReportActivityEvent;
import org.example.bff.kafka.producer.AbstractProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReportActivityProducer extends AbstractProducer<ReportActivityEvent> {

    private static final String TOPIC = "report.activity";

    public ReportActivityProducer(KafkaTemplate<String, ReportActivityEvent> kafkaTemplate) {
        super(kafkaTemplate, TOPIC);
    }
}
