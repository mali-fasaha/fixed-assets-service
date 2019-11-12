package io.github.assets.app.messaging.fileUpload;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.EntityResource;
import io.github.assets.app.messaging.Mapping;
import io.github.assets.app.messaging.assetDepreciation.AssetDepreciationMTO;
import io.github.assets.app.resource.decorator.IFileUploadResource;
import io.github.assets.domain.FileUpload;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.FileUploadDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URISyntaxException;

@Transactional
@Service("fileUploadEntityResource")
public class FileUploadEntityResource implements EntityResource<FileUploadMTO, DeleteMessageDTO> {

    private final IFileUploadResource fileUploadResourceDecorator;
    private final Mapping<FileUploadDTO, FileUploadMTO> assetMTOMapping;
    private final MessageTokenService messageTokenService;

    public FileUploadEntityResource(final IFileUploadResource fileUploadResourceDecorator, final Mapping<FileUploadDTO, FileUploadMTO> fileUploadMTOMapping, final MessageTokenService messageTokenService) {
        this.fileUploadResourceDecorator = fileUploadResourceDecorator;
        this.assetMTOMapping = fileUploadMTOMapping;
        this.messageTokenService = messageTokenService;
    }

    @Override
    public void createEntity(final FileUploadMTO entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException {

        ResponseEntity response = fileUploadResourceDecorator.createFileUpload(assetMTOMapping.toValue1(entityMTO));

        if (response.getStatusCode().is2xxSuccessful()) {
            messageToken.setActioned(true);
        }

        messageTokenService.save(messageToken);
    }

    @Override
    public void updateEntity(final FileUploadMTO entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException {

        ResponseEntity response = fileUploadResourceDecorator.updateFileUpload(assetMTOMapping.toValue1(entityMTO));

        if (response.getStatusCode().is2xxSuccessful()) {
            messageToken.setActioned(true);
        }

        messageTokenService.save(messageToken);
    }

    @Override
    public void deleteEntity(final DeleteMessageDTO deleteMessageDTO, final MessageTokenDTO messageToken) {

        ResponseEntity response = fileUploadResourceDecorator.deleteFileUpload(deleteMessageDTO.getId());

        if (response.getStatusCode().is2xxSuccessful()) {
            messageToken.setActioned(true);
        }

        messageTokenService.save(messageToken);
    }
}
