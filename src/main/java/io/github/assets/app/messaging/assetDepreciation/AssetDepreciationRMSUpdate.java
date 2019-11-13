package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.Mapping;
import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.service.dto.AssetDepreciationDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service("assetDepreciationRMSUpdate")
public class AssetDepreciationRMSUpdate implements MessageService<AssetDepreciationDTO> {

    private final Mapping<AssetDepreciationDTO, AssetDepreciationMTO> assetMTOMapper;
    private final MessageService<TokenizableMessage<String>> messageService;

    public AssetDepreciationRMSUpdate(final Mapping<AssetDepreciationDTO, AssetDepreciationMTO> assetMTOMapper, MessageService<TokenizableMessage<String>> assetDepreciationUpdateMessageService) {
        this.assetMTOMapper = assetMTOMapper;
        this.messageService = assetDepreciationUpdateMessageService;
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param message This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    @Override
    public MessageTokenDTO sendMessage(final AssetDepreciationDTO message) {

        // TODO update timestamp
        log.debug("Al a carte update api has received entity {} and is enqueuing to the stream...", message);

        return messageService.sendMessage(assetMTOMapper.toValue2(message));
    }
}
