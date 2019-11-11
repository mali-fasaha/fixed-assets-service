package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.assetAcquisition.AssetAcquisitionResourceStreams;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface AssetDepreciationResourceStreams {

    String CREATE_RESOURCE_IN = "asset-depreciation-resource-create-in";
    String CREATE_RESOURCE_OUT = "asset-depreciation-resource-create-out";
    String UPDATE_RESOURCES_IN = "asset-depreciation-resource-update-in";
    String UPDATE_RESOURCES_OUT = "asset-depreciation-resource-update-out";
    String DELETE_RESOURCES_IN = "asset-depreciation-resource-delete-in";
    String DELETE_RESOURCES_OUT = "asset-depreciation-resource-delete-out";

    @Input(CREATE_RESOURCE_IN)
    SubscribableChannel inboundCreateResource();

    @Output(CREATE_RESOURCE_OUT)
    MessageChannel outboundCreateResource();

    @Input(UPDATE_RESOURCES_IN)
    SubscribableChannel inboundUpdateResource();

    @Output(UPDATE_RESOURCES_OUT)
    MessageChannel outboundUpdateResource();

    @Input(DELETE_RESOURCES_IN)
    SubscribableChannel inboundDeleteResource();

    @Output(DELETE_RESOURCES_OUT)
    MessageChannel outboundDeleteResource();
}
