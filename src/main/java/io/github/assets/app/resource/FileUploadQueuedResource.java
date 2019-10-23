package io.github.assets.app.resource;

import io.github.assets.app.resource.decorator.IFileUploadResource;
import io.github.assets.service.dto.FileUploadDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URISyntaxException;

@Transactional
@Service("fileUploadQueuedResource")
public class FileUploadQueuedResource implements QueuedResource<FileUploadDTO> {

    private final IFileUploadResource fileUploadResource;

    public FileUploadQueuedResource(final IFileUploadResource fileUploadResource) {
        this.fileUploadResource = fileUploadResource;
    }

    /**
     * Create entity response
     */
    @Override
    public ResponseEntity<FileUploadDTO> createEntity(final FileUploadDTO requestDTO) throws URISyntaxException {
        return fileUploadResource.createFileUpload(requestDTO);
    }

    /**
     * Update entity sent in the request
     */
    @Override
    public ResponseEntity<FileUploadDTO> updateEntity(final FileUploadDTO requestDTO) throws URISyntaxException {
        return fileUploadResource.updateFileUpload(requestDTO);
    }

    /**
     * Delete the entity whose ID
     */
    @Override
    public ResponseEntity<Void> deleteEntity(final Long id) {
        return fileUploadResource.deleteFileUpload(id);
    }
}
