package io.github.assets.app.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface FileNotificationStreams {

    String NOTIFICATION_INPUT = "file-notifications-in";
    String NOTIFICATION_OUT = "file-notifications-out";

    /**
     * Defines an inbound stream to read messages from a Kafka topic
     */
    @Input(NOTIFICATION_INPUT)
    SubscribableChannel inboundNotifications();

    /**
     * Outbound stream to write messages to a kafka topic
     */
    @Output(NOTIFICATION_OUT)
    MessageChannel outboundNotifications();
}
