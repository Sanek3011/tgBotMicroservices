package org.example.bff.kafka.producer.reportProducers;

import org.example.bff.kafka.events.reportEvents.ReportProcessEvent;
import org.example.bff.kafka.producer.AbstractProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReportProcessProducer extends AbstractProducer<ReportProcessEvent> {

    public static final String TOPIC = "report.process";

    public ReportProcessProducer(KafkaTemplate<String, ReportProcessEvent> kafkaTemplate) {
        super(kafkaTemplate, TOPIC);
    }
}
