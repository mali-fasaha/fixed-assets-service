package io.github.assets.app.messaging.fileUpload;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.EntityResource;
import io.github.assets.app.messaging.ResponsiveEntityResource;
import io.github.assets.service.dto.FileUploadDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;

@Transactional
@Service("fileUploadEntityResource")
public class FileUploadEntityResource implements EntityResource<FileUploadMTO, DeleteMessageDTO> {

    private final ResponsiveEntityResource<FileUploadMTO, DeleteMessageDTO, FileUploadDTO> fileUploadResponsiveEntityResource;

    public FileUploadEntityResource(final ResponsiveEntityResource<FileUploadMTO, DeleteMessageDTO, FileUploadDTO> fileUploadResponsiveEntityResource) {
        this.fileUploadResponsiveEntityResource = fileUploadResponsiveEntityResource;
    }

    @Override
    public void createEntity(final FileUploadMTO entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException {

        fileUploadResponsiveEntityResource.createEntity(entityMTO, messageToken);
    }

    @Override
    public void updateEntity(final FileUploadMTO entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException {

        fileUploadResponsiveEntityResource.updateEntity(entityMTO, messageToken);
    }

    @Override
    public void deleteEntity(final DeleteMessageDTO deleteMessageDTO, final MessageTokenDTO messageToken) {

        fileUploadResponsiveEntityResource.deleteEntity(deleteMessageDTO, messageToken);
    }
}
