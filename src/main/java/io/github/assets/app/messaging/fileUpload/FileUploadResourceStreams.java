package io.github.assets.app.messaging.fileUpload;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface FileUploadResourceStreams {

    String CREATE_RESOURCE_IN = "file-upload-resource-create-in";
    String CREATE_RESOURCE_OUT = "file-upload-resource-create-out";
    String UPDATE_RESOURCES_IN = "file-upload-resource-update-in";
    String UPDATE_RESOURCES_OUT = "file-upload-resource-update-out";
    String DELETE_RESOURCES_IN = "file-upload-resource-delete-in";
    String DELETE_RESOURCES_OUT = "file-upload-resource-delete-out";

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
