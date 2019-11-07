package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.StringedTokenMessageService;
import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.domain.MessageToken;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.MessageTokenDTO;
import io.github.assets.service.mapper.MessageTokenMapper;
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

    private final MessageTokenService messageTokenService;
    private final TokenGenerator tokenGenerator;
    private final AssetAcquisitionResourceStreams assetAcquisitionResourceStreams;
    private final MessageService<TokenizableMessage<String>> messageService;

    public AssetAcquisitionRMSDelete(final MessageTokenService messageTokenService, final TokenGenerator tokenGenerator, final AssetAcquisitionResourceStreams assetAcquisitionResourceStreams, final MessageTokenMapper messageTokenMapper) {
        this.messageTokenService = messageTokenService;
        this.tokenGenerator = tokenGenerator;
        this.assetAcquisitionResourceStreams = assetAcquisitionResourceStreams;

        messageService = new StringedTokenMessageService(tokenGenerator, messageTokenService, assetAcquisitionResourceStreams.outboundDeleteResource(),
                                                                                                    messageTokenMapper);
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param message This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    @Override
    public MessageTokenDTO sendMessage(final Long message) {

        // TODO update timestamp
        log.debug("Al a carte delete api has received request for Id {} and is enqueuing to the stream...", message);

        return messageService.sendMessage(DeleteMessageDTO.builder()
                                                          .id(message)
                                                          .description("Delete Asset-Acquisition-Entity request id : " + message)
                                                          .build());
    }
}
