package io.github.assets.app.messaging.core;

/**
 * Generalised representation of a producer with type T message. Messages are sent to runtime topics
 *
 * @param <T>
 */
public interface AppProducer<T> {

    void init();

    void send(T message, String topic);

    void shutdown();
}
