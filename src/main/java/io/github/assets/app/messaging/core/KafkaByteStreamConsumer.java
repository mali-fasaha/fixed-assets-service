package io.github.assets.app.messaging.core;

import io.github.assets.config.KafkaProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service("kafkaByteStreamConsumer")
public class KafkaByteStreamConsumer implements ReadableConsumer<String,byte[]>, AppConsumer<KafkaConsumer<String, byte[]>, RecordReader<String, byte[]>> {

    private final AtomicBoolean closed = new AtomicBoolean(false);

    private final KafkaProperties kafkaProperties;

    private KafkaConsumer<String, byte[]> kafkaConsumer;

    public KafkaByteStreamConsumer(final KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    public void start(Collection<String> topics, RecordReader<String, byte[]> consumer) {
        log.info("Kafka consumer starting...");
        this.kafkaConsumer = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        Thread consumerThread = new Thread(() -> {
            try {
                kafkaConsumer.subscribe(topics);
                log.info("Kafka consumer started");
                while (!closed.get()) {
                    ConsumerRecords<String, byte[]> records = kafkaConsumer.poll(Duration.ofSeconds(3));
                    for (ConsumerRecord<String, byte[]> record : records) {
                        log.info("Consumed message in {} : {}", record.topic(), record.value());
                        consumer.accept(record);
                    }
                }
                kafkaConsumer.commitSync();
            } catch (WakeupException e) {
                // Ignore exception if closing
                if (!closed.get()) {
                    throw e;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                kafkaConsumer.close();
            }
        });
        consumerThread.start();
    }

    public KafkaConsumer<String, byte[]> getKafkaConsumer() {
        return kafkaConsumer;
    }

    public void shutdown() {
        log.info("Shutdown Kafka consumer");
        closed.set(true);
        kafkaConsumer.wakeup();
    }
}
