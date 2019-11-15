package io.github.assets.app.messaging.fileUpload;

import io.github.assets.app.Mapping;
import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.service.dto.FileUploadDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service("fileUploadRMSCreate")
public class FileUploadRMSCreate implements MessageService<FileUploadDTO> {
    private final Mapping<FileUploadDTO, FileUploadMTO> assetMTOMapper;
    private final MessageService<TokenizableMessage<String>> messageService;

    public FileUploadRMSCreate(final Mapping<FileUploadDTO, FileUploadMTO> fileUploadMTOMapping, final MessageService<TokenizableMessage<String>> fileUploadCreateMessageService) {
        this.assetMTOMapper = fileUploadMTOMapping;
        this.messageService = fileUploadCreateMessageService;
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param message This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    @Override
    public MessageTokenDTO sendMessage(final FileUploadDTO message) {

        // TODO update timestamp
        log.debug("Al a carte create api has received entity {} and is enqueuing to the stream...", message);

        return messageService.sendMessage(assetMTOMapper.toValue2(message));
    }
}
