package io.github.assets.app.messaging.core;

import java.util.Collection;

public interface AppConsumer<K, C> {

    /**
     * Start the consume and subscribe to the topics in the collection and
     * consume the message through the interface implemented for record consumer
     *
     * @param topics
     * @param recordConsumer
     */
    void start(Collection<String> topics, C recordConsumer);

    K getKafkaConsumer();

    void shutdown();
}
