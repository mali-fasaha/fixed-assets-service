package io.github.assets.app.messaging;

public interface MuteListener<Payload> {

    void handleMessage(Payload payload);
}
