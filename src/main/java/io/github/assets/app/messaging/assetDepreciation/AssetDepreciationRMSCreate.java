package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.Mapping;
import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.service.dto.AssetDepreciationDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Transactional
@Service("assetDepreciationRMSCreate")
public class AssetDepreciationRMSCreate implements MessageService<AssetDepreciationDTO> {

    private final Mapping<AssetDepreciationDTO, AssetDepreciationMTO> assetMTOMapper;
    private final MessageService<TokenizableMessage<String>> messageService;

    public AssetDepreciationRMSCreate(final Mapping<AssetDepreciationDTO, AssetDepreciationMTO> assetDepreciationMTOMapping,
                                      final MessageService<TokenizableMessage<String>> assetDepreciationCreateMessageService) {
        this.assetMTOMapper = assetDepreciationMTOMapping;
        this.messageService = assetDepreciationCreateMessageService;
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
        log.debug("Al a carte create api has received entity {} and is enqueuing to the stream...", message);

        return messageService.sendMessage(assetMTOMapper.toValue2(message));
    }
}
