package io.github.assets.app.messaging.jsonStrings;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface JsonStringStreams {
    String JSON_INBOUND = "json-create-inbound";
    String JSON_OUTBOUND = "json-create-outbound";

    @Input(JSON_INBOUND)
    SubscribableChannel inbound();

    @Output(JSON_OUTBOUND)
    MessageChannel outbound();
}
