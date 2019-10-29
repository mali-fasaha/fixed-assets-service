package io.github.assets.app.messaging.core;

public interface Producer<T> {

    void init();

    void send(T message, String topic);

    void shutdown();
}
