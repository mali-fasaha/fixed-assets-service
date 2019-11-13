package io.github.assets.app.messaging.fileUpload;

import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.MutationResource;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.FileUploadDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URISyntaxException;

@Slf4j
@Transactional
@Service("fileUploadMutationResource")
public class FileUploadMutationResource implements MutationResource<FileUploadDTO> {

    private final MessageTokenService messageTokenService;
    private final MessageService<FileUploadDTO> createMessageService;
    private final MessageService<FileUploadDTO> updateMessageService;
    private final MessageService<Long> deleteMessageService;

    public FileUploadMutationResource(final MessageTokenService messageTokenService, final MessageService<FileUploadDTO> fileUploadRMSCreate, final MessageService<FileUploadDTO> fileUploadRMSUpdate,
                                      final MessageService<Long> fileUploadRMSDelete) {
        this.messageTokenService = messageTokenService;
        this.createMessageService = fileUploadRMSCreate;
        this.updateMessageService = fileUploadRMSUpdate;
        this.deleteMessageService = fileUploadRMSDelete;
    }

    @Override
    public MessageTokenDTO createEntity(final FileUploadDTO FileUploadDTO) throws URISyntaxException {
        log.debug("Request to create entity received for action : {} and delegated to al a carte creation api", FileUploadDTO);
        MessageTokenDTO tokenForEnqueued = createMessageService.sendMessage(FileUploadDTO);
        tokenForEnqueued.setContentFullyEnqueued(true);
        return messageTokenService.save(tokenForEnqueued);
    }

    @Override
    public MessageTokenDTO updateEntity(final FileUploadDTO FileUploadDTO) throws URISyntaxException {
        log.debug("Request to update entity id : {} received and delegated to al a carte update api", FileUploadDTO);
        MessageTokenDTO tokenForEnqueued = updateMessageService.sendMessage(FileUploadDTO);
        tokenForEnqueued.setContentFullyEnqueued(true);
        return messageTokenService.save(tokenForEnqueued);
    }

    @Override
    public MessageTokenDTO deleteEntity(final Long id) {
        log.debug("Request to delete entity id : {} received and delegated to al a carte deletion api", id);
        MessageTokenDTO tokenForEnqueued = deleteMessageService.sendMessage(id);
        tokenForEnqueued.setContentFullyEnqueued(true);
        return messageTokenService.save(tokenForEnqueued);
    }
}
