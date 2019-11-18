package io.github.assets.app.messaging.jsonStrings;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface JsonStringStreams {
    String JSON_ACQUISITIONS_CREATE_INBOUND = "json-acquisitions-create-acquisitionsCreateInbound";
    String JSON_ACQUISITIONS_CREATE_OUTBOUND = "json-acquisitions-create-acquisitionsCreateOutbound";

    @Input(JSON_ACQUISITIONS_CREATE_INBOUND)
    SubscribableChannel acquisitionsCreateInbound();

    @Output(JSON_ACQUISITIONS_CREATE_OUTBOUND)
    MessageChannel acquisitionsCreateOutbound();
}
