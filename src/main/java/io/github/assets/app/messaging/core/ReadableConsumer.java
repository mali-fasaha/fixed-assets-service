package io.github.assets.app.messaging.core;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collection;

/**
 * This is a Kafka consumer that allows a record-reader implementation to poll messages.
 * This is a facade to simplify the declaration and definition of the
 * AppConsumer<KafkaConsumer<T, T>,RecordReader<T>> interface that assumes we are using a
 * kafka-consumer
 *
 * @param <T>
 */
public interface ReadableConsumer<T> extends AppConsumer<KafkaConsumer<T, T>,RecordReader<T>> {

    /**
     * Start the consume and subscribe to the topics in the collection and consume the message through the interface implemented for record consumer
     */
    @Override
    void start(Collection<String> topics, RecordReader<T> recordConsumer);
}
