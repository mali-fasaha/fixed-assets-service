package io.github.assets.app.messaging;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MessageStreams {

    SubscribableChannel inbound();

    MessageChannel outbound();
}
