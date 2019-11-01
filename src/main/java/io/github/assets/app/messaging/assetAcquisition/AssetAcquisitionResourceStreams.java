package io.github.assets.app.messaging.assetAcquisition;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Stream definitions for resource actions
 */
public interface AssetAcquisitionResourceStreams {

    String CREATE_RESOURCE_IN = "queued-resources-create-in";
    String CREATE_RESOURCE_OUT = "queued-resources-create-out";
    String UPDATE_RESOURCES_IN = "queued-resources-update-in";
    String UPDATE_RESOURCES_OUT = "queued-resources-update-out";
    String DELETE_RESOURCES_IN = "queued-resources-update-in";
    String DELETE_RESOURCES_OUT = "queued-resources-update-out";

    @Input(AssetAcquisitionResourceStreams.CREATE_RESOURCE_IN)
    SubscribableChannel inboundCreateResource();

    @Input(AssetAcquisitionResourceStreams.CREATE_RESOURCE_OUT)
    MessageChannel outboundCreateResource();

    @Input(AssetAcquisitionResourceStreams.UPDATE_RESOURCES_IN)
    SubscribableChannel inboundUpdateResource();

    @Output(AssetAcquisitionResourceStreams.UPDATE_RESOURCES_OUT)
    MessageChannel outboundUpdateResource();

    @Input(AssetAcquisitionResourceStreams.DELETE_RESOURCES_IN)
    SubscribableChannel inboundDeleteResource();

    @Output(AssetAcquisitionResourceStreams.DELETE_RESOURCES_OUT)
    MessageChannel outboundDeleteResource();

}