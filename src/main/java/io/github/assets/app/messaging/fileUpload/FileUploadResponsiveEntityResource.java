package io.github.assets.app.messaging.fileUpload;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.EntityResource;
import io.github.assets.app.messaging.Mapping;
import io.github.assets.app.messaging.ResponsiveEntityResource;
import io.github.assets.app.resource.decorator.IFileUploadResource;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.FileUploadDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;

@Transactional
@Service("fileUploadResponsiveEntityResource")
public class FileUploadResponsiveEntityResource implements ResponsiveEntityResource<FileUploadMTO, DeleteMessageDTO, FileUploadDTO>, EntityResource<FileUploadMTO, DeleteMessageDTO> {

    private final IFileUploadResource fileUploadResourceDecorator;
    private final Mapping<FileUploadDTO, FileUploadMTO> assetMTOMapping;
    private final MessageTokenService messageTokenService;

    public FileUploadResponsiveEntityResource(final IFileUploadResource fileUploadResourceDecorator, final Mapping<FileUploadDTO, FileUploadMTO> fileUploadMTOMapping,
                                              final MessageTokenService messageTokenService) {
        this.fileUploadResourceDecorator = fileUploadResourceDecorator;
        this.assetMTOMapping = fileUploadMTOMapping;
        this.messageTokenService = messageTokenService;
    }

    @Override
    public ResponseEntity<FileUploadDTO> createEntityAndRespond(final FileUploadMTO entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException {

        ResponseEntity<FileUploadDTO> response = fileUploadResourceDecorator.createFileUpload(assetMTOMapping.toValue1(entityMTO));

        if (response.getStatusCode().is2xxSuccessful()) {
            messageToken.setActioned(true);
        }
        messageTokenService.save(messageToken);

        return response;
    }

    @Override
    public ResponseEntity<FileUploadDTO> updateEntityAndRespond(final FileUploadMTO entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException {
        ResponseEntity<FileUploadDTO> response = fileUploadResourceDecorator.updateFileUpload(assetMTOMapping.toValue1(entityMTO));

        if (response.getStatusCode().is2xxSuccessful()) {
            messageToken.setActioned(true);
        }
        messageTokenService.save(messageToken);

        return response;
    }

    @Override
    public ResponseEntity<Void> deleteEntityAndRespond(final DeleteMessageDTO deleteMessageDTO, final MessageTokenDTO messageToken) {

        ResponseEntity<Void> response = fileUploadResourceDecorator.deleteFileUpload(deleteMessageDTO.getId());

        if (response.getStatusCode().is2xxSuccessful()) {
            messageToken.setActioned(true);
        }
        messageTokenService.save(messageToken);

        return response;
    }
}
