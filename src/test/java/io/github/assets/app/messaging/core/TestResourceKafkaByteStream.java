package io.github.assets.app.messaging.core;

import io.github.assets.web.rest.FixedAssetServiceKafkaResource;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static io.github.assets.app.AppConstants.GENERAL_KAFKA_STRING_TOPIC;

@RestController
@RequestMapping("/api/fixed-asset-service-kafka")
public class TestResourceKafkaByteStream {
    private final Logger log = LoggerFactory.getLogger(FixedAssetServiceKafkaResource.class);

    private AppProducer<byte[]> kafkaProducer;

    public TestResourceKafkaByteStream(final AppProducer<byte[]> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish-test-bytes")
    public void sendMessageToKafkaTopic(@RequestBody TestMessage message) {
        this.kafkaProducer.send(SerializationUtils.serialize(message), GENERAL_KAFKA_STRING_TOPIC);
    }
}
