package io.github.assets.app.messaging.fileNotification;

import io.github.assets.app.messaging.MessageStreams;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface FileNotificationStreams extends MessageStreams {

    String INPUT = "file-notifications-in";
    String OUTPUT = "file-notifications-out";

    @Input(INPUT)
    SubscribableChannel inbound();
    @Output(OUTPUT)
    MessageChannel outbound();
}
