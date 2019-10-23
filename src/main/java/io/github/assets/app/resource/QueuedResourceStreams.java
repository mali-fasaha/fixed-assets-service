package io.github.assets.app.resource;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Stream definitions for resource actions
 */
public interface QueuedResourceStreams {

    String QUEUED_RESOURCES_CREATE = "queued-resources-create-in";
    String QUEUED_RESOURCES_UPDATE = "queued-resources-update-in";
    String QUEUED_RESOURCES_OUT = "queued-resources-out";

    /**
     * Defines an inbound stream to read messages from a Kafka topic
     */
    @Input(QUEUED_RESOURCES_CREATE)
    SubscribableChannel inboundQueuedCreateRequests();

    /**
     * Defines an inbound stream to read messages from a Kafka topic
     */
    @Input(QUEUED_RESOURCES_UPDATE)
    SubscribableChannel inboundQueuedUpdateRequests();

    /**
     * Outbound stream to write messages to a kafka topic
     */
    @Output(QUEUED_RESOURCES_OUT)
    MessageChannel outboundQueuedResources();
}
