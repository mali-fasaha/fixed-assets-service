package io.github.assets.app.messaging;

public interface ResponsiveListener<Payload, Response> extends MuteListener<Payload> {

    Response attendMessage(Payload payload);

    default void handleMessage(Payload payload) {
        this.attendMessage(payload);
    }
}
