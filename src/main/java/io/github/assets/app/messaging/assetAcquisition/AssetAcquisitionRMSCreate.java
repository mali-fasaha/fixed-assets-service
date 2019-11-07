package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.Mapping;
import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.StringedTokenMessageService;
import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import io.github.assets.service.mapper.MessageTokenMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Asset-acquisition-resource-message-service for create method
 */
@Slf4j
@Transactional
@Service("assetAcquisitionRMSCreate")
public class AssetAcquisitionRMSCreate implements MessageService<AssetAcquisitionDTO> {

    private final Mapping<AssetAcquisitionDTO, AssetAcquisitionMTO> assetAcquisitionMTOMapper;

    private final MessageService<TokenizableMessage<String>> messageService;

    public AssetAcquisitionRMSCreate(final MessageTokenService messageTokenService, final TokenGenerator tokenGenerator, final AssetAcquisitionResourceStreams assetAcquisitionResourceStreams,
                                     final Mapping<AssetAcquisitionDTO, AssetAcquisitionMTO> assetAcquisitionMTOMapper, final MessageTokenMapper messageTokenMapper) {
        this.assetAcquisitionMTOMapper = assetAcquisitionMTOMapper;

        messageService = new StringedTokenMessageService(tokenGenerator, messageTokenService, assetAcquisitionResourceStreams.outboundCreateResource(), messageTokenMapper);
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @return This is the token for the message that has just been sent
     */
    public MessageTokenDTO sendMessage(final AssetAcquisitionDTO assetAcquisitionDTO) {

        // TODO update timestamp
        log.debug("Al a carte create api has received entity {} and is enqueuing to the stream...", assetAcquisitionDTO);

        return messageService.sendMessage(assetAcquisitionMTOMapper.toValue2(assetAcquisitionDTO));
    }

}
