package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.Mapping;
import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

/**
 * Asset-acquisition-resource-message-service for update method
 */
@Slf4j
@Transactional
@Service("assetAcquisitionRMSUpdate")
public class AssetAcquisitionRMSUpdate implements MessageService<AssetAcquisitionDTO> {

    private final Mapping<AssetAcquisitionDTO, AssetAcquisitionMTO> assetMTOMapper;
    private MessageService<TokenizableMessage<String>> messageService;

    public AssetAcquisitionRMSUpdate(Mapping<AssetAcquisitionDTO, AssetAcquisitionMTO> assetAcquisitionMTOMapper, MessageService<TokenizableMessage<String>> assetAcquisitionUpdateMessageService) {
        this.assetMTOMapper = assetAcquisitionMTOMapper;
        messageService = assetAcquisitionUpdateMessageService;
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @return This is the token for the message that has just been sent
     */
    public MessageTokenDTO sendMessage(final AssetAcquisitionDTO message) {

        // ? update timestamp
        log.debug("Al a carte update api has received entity {} and is enqueuing to the stream...", message);

        return messageService.sendMessage(assetMTOMapper.toValue2(message));
    }
}
