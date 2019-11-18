package io.github.assets.app.messaging.fileUpload;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.EntityResource;
import io.github.assets.app.messaging.ResponsiveEntityResource;
import io.github.assets.service.dto.FileUploadDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;

/**
 *  TODO Create interface for disseminating file information
 */
@Transactional
@Service("fileUploadEntityResource")
public class FileUploadEntityResource implements EntityResource<FileUploadMTO, DeleteMessageDTO> {

    private final ResponsiveEntityResource<FileUploadMTO, DeleteMessageDTO, FileUploadDTO> fileUploadResponsiveEntityResource;

    public FileUploadEntityResource(final ResponsiveEntityResource<FileUploadMTO, DeleteMessageDTO, FileUploadDTO> fileUploadResponsiveEntityResource) {
        this.fileUploadResponsiveEntityResource = fileUploadResponsiveEntityResource;
    }

    @Override
    public void createEntity(final FileUploadMTO entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException {

        // TODO Make use of the response by implementing the correct interface
        fileUploadResponsiveEntityResource.createEntity(entityMTO, messageToken);
    }

    @Override
    public void updateEntity(final FileUploadMTO entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException {

        // TODO Make use of the response by implementing the correct interface
        fileUploadResponsiveEntityResource.updateEntity(entityMTO, messageToken);
    }

    @Override
    public void deleteEntity(final DeleteMessageDTO deleteMessageDTO, final MessageTokenDTO messageToken) {

        // TODO Make use of the response by implementing the correct interface
        fileUploadResponsiveEntityResource.deleteEntity(deleteMessageDTO, messageToken);
    }
}
