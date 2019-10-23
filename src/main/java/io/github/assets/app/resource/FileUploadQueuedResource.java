package io.github.assets.app.resource;

import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.resource.decorator.IFileUploadResource;
import io.github.assets.domain.FileType;
import io.github.assets.domain.MessageToken;
import io.github.assets.service.FileTypeService;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.FileUploadDTO;
import io.github.assets.service.impl.MessageTokenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URISyntaxException;

/**
 * Delegate for sending requests to the queue. In effect this class is to act as a producer
 *
 */
@Transactional
@Service("fileUploadQueuedResource")
public class FileUploadQueuedResource implements QueuedResource<FileUploadDTO> {

    private final IFileUploadResource fileUploadResource;
    @Autowired
    private MessageService<ResourceMessage<FileUploadDTO>> resourceMessageService;
    @Autowired
    private FileTypeService fileTypeService;
    @Autowired
    private MessageTokenService messageTokenService;


    public FileUploadQueuedResource(final IFileUploadResource fileUploadResource) {
        this.fileUploadResource = fileUploadResource;
    }

    /**
     * Create entity response
     */
    @Override
    public ResponseEntity<FileUploadDTO> createEntity(final FileUploadDTO requestDTO) throws URISyntaxException {
//        return fileUploadResource.createFileUpload(requestDTO);
        FileType fileType = fileTypeService.findOne(requestDTO.getFileTypeId()).get();

        MessageToken result = messageTokenService.save(this.resourceMessageService.sendMessage(new ResourceMessage<>(requestDTO, fileType.getFileType())));
        // TODO send result to message-token queue

        return ResponseEntity.ok(requestDTO);
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
