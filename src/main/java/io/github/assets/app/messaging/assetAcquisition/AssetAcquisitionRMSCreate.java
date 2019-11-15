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
 * Asset-acquisition-resource-message-service for create method
 */
@Slf4j
@Transactional
@Service("assetAcquisitionRMSCreate")
public class AssetAcquisitionRMSCreate implements MessageService<AssetAcquisitionDTO> {

    private final Mapping<AssetAcquisitionDTO, AssetAcquisitionMTO> assetAcquisitionMTOMapper;
    private final MessageService<TokenizableMessage<String>> messageService;


    public AssetAcquisitionRMSCreate(final Mapping<AssetAcquisitionDTO, AssetAcquisitionMTO> assetAcquisitionMTOMapper,
                                     final MessageService<TokenizableMessage<String>> assetAcquisitionCreateMessageService) {
        this.assetAcquisitionMTOMapper = assetAcquisitionMTOMapper;
        this.messageService = assetAcquisitionCreateMessageService;
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @return This is the token for the message that has just been sent
     */
    public MessageTokenDTO sendMessage(final AssetAcquisitionDTO assetAcquisitionDTO) {

        // ? update timestamp
        log.debug("Al a carte create api has received entity {} and is enqueuing to the stream...", assetAcquisitionDTO);

        return messageService.sendMessage(assetAcquisitionMTOMapper.toValue2(assetAcquisitionDTO));
    }

}
