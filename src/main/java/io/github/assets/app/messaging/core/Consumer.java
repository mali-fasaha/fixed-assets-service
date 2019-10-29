package io.github.assets.app.messaging.core;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collection;

public interface Consumer<T> {

    /**
     * Start the consume and subscribe to the topics in the collection
     *
     * @param topics
     */
    void start(Collection<String> topics);

    T getKafkaConsumer();

    void shutdown();
}
