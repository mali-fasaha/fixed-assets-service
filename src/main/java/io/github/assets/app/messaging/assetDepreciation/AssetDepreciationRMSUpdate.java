package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.MessageService;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Transactional
@Service("assetDepreciationRMSUpdate")
public class AssetDepreciationRMSUpdate implements MessageService<AssetAcquisitionDTO> {

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param message This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    @Override
    public MessageTokenDTO sendMessage(final AssetAcquisitionDTO message) {
        return null;
    }
}
