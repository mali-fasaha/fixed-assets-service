package io.github.assets.app.messaging.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service("kafkaByteStreamProducer")
public class KafkaByteStreamProducer implements AppProducer<byte[]> {

    private final KafkaByteArrayProperties kafkaByteArrayProperties;

    private KafkaProducer<byte[], byte[]> kafkaProducer;

    public KafkaByteStreamProducer(final KafkaByteArrayProperties kafkaByteArrayProperties) {
        this.kafkaByteArrayProperties = kafkaByteArrayProperties;
    }

    @Override
    public void init() {
        log.info("Kafka string producer initializing...");
        kafkaProducer = new KafkaProducer<>(kafkaByteArrayProperties.getProducerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        log.info("Kafka string producer initialized");
    }

    @Override
    public void send(final byte[] message, final String topic) {
        ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(topic, message);
        try {
            kafkaProducer.send(record);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void shutdown() {
        log.info("Shutdown Kafka producer");
        kafkaProducer.close();
    }
}
