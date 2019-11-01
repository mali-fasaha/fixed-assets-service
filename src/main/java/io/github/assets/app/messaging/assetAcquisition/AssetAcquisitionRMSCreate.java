package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.Mapping;
import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.StringedTokenMessageService;
import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.domain.MessageToken;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.AssetAcquisitionDTO;
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

    private final MessageTokenService messageTokenService;
    private final TokenGenerator tokenGenerator;
    private final AssetAcquisitionResourceStreams assetAcquisitionResourceStreams;
    private final Mapping<AssetAcquisitionDTO, AssetAcquisitionMTO> assetAcquisitionMTOMapper;

    public AssetAcquisitionRMSCreate(final MessageTokenService messageTokenService, final TokenGenerator tokenGenerator, final AssetAcquisitionResourceStreams assetAcquisitionResourceStreams,
                                     final Mapping<AssetAcquisitionDTO, AssetAcquisitionMTO> assetAcquisitionMTOMapper) {
        this.messageTokenService = messageTokenService;
        this.tokenGenerator = tokenGenerator;
        this.assetAcquisitionResourceStreams = assetAcquisitionResourceStreams;
        this.assetAcquisitionMTOMapper = assetAcquisitionMTOMapper;
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @return This is the token for the message that has just been sent
     */
    public MessageToken sendMessage(final AssetAcquisitionDTO assetAcquisitionDTO) {

        MessageService<TokenizableMessage<String>> messageService = new StringedTokenMessageService(tokenGenerator, messageTokenService, assetAcquisitionResourceStreams.outboundCreateResource());

        return messageService.sendMessage(assetAcquisitionMTOMapper.toValue2(assetAcquisitionDTO));
    }

}
