package io.github.assets.app.resource;

import io.github.assets.app.resource.decorator.IFileUploadResource;
import io.github.assets.service.dto.FileUploadDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

@Slf4j
@Component
public class FileUploadQueuedResourceListener implements QueuedResource<FileUploadDTO> {

    private final IFileUploadResource fileUploadResource;

    public FileUploadQueuedResourceListener(final IFileUploadResource fileUploadResource) {
        this.fileUploadResource = fileUploadResource;
    }

    /**
     * Create entity response
     */
    @Override
    @StreamListener(QueuedResourceStreams.QUEUED_RESOURCES_CREATE)
    public ResponseEntity<FileUploadDTO> createEntity(@Payload FileUploadDTO requestDTO) throws URISyntaxException {

        log.info("Enqueued request for {} received for create request", requestDTO);

        // TODO use actual services
        fileUploadResource.createEntity(requestDTO);

        return ResponseEntity.ok(requestDTO);
    }

    /**
     * Update entity sent in the request
     */
    @Override
    @StreamListener(QueuedResourceStreams.QUEUED_RESOURCES_UPDATE)
    public ResponseEntity<FileUploadDTO> updateEntity(@Payload FileUploadDTO requestDTO) throws URISyntaxException {

        log.info("Enqueued request for {} received for update", requestDTO);

        // TODO use actual services
        fileUploadResource.updateEntity(requestDTO);

        return ResponseEntity.ok(requestDTO);
    }

    /**
     * Delete the entity whose ID
     */
    @Override
    public ResponseEntity<Void> deleteEntity(final Long id) {
        return null;
    }
}
