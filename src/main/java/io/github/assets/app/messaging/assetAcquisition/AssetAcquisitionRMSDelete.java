package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Asset-acquisition-resource-message-service for delete method
 */
@Slf4j
@Transactional
@Service("assetAcquisitionRMSDelete")
public class AssetAcquisitionRMSDelete implements MessageService<Long> {

    private final MessageService<TokenizableMessage<String>> messageService;

    public AssetAcquisitionRMSDelete(final MessageService<TokenizableMessage<String>> assetAcquisitionDeleteMessageService) {

        messageService = assetAcquisitionDeleteMessageService;
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param message This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    @Override
    public MessageTokenDTO sendMessage(final Long message) {

        // ? update timestamp
        log.debug("Al a carte delete api has received request for Id {} and is enqueuing to the stream...", message);

        return messageService.sendMessage(DeleteMessageDTO.builder().id(message).description("Delete Asset-Entity request id : " + message).build());
    }
}
