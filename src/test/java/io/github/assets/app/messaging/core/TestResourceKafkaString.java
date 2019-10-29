package io.github.assets.app.messaging.core;

import io.github.assets.web.rest.FixedAssetServiceKafkaResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static io.github.assets.app.AppConstants.GENERAL_KAFKA_STRING_TOPIC;

@RestController
@RequestMapping("/api/fixed-asset-service-kafka")
public class TestResourceKafkaString {
    private final Logger log = LoggerFactory.getLogger(FixedAssetServiceKafkaResource.class);

    private AppProducer<String> kafkaProducer;

    public TestResourceKafkaString(final AppProducer<String> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish-test-string")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        log.debug("REST request to send to Kafka topic the message : {}", message);
        this.kafkaProducer.send(message, GENERAL_KAFKA_STRING_TOPIC);
    }
}
