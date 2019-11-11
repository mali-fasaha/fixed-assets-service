package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.MessageService;
import io.github.assets.service.dto.AssetDepreciationDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Transactional
@Service("assetDepreciationRMSCreate")
public class AssetDepreciationRMSCreate implements MessageService<AssetDepreciationDTO> {

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param message This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    @Override
    public MessageTokenDTO sendMessage(final AssetDepreciationDTO message) {
        return null;
    }
}
