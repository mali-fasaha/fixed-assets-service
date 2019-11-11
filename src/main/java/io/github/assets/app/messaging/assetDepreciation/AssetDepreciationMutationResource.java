package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.MutationResource;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.AssetDepreciationDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;

@Slf4j
@Transactional
@Service("assetDepreciationMutationResource")
public class AssetDepreciationMutationResource implements MutationResource<AssetDepreciationDTO> {

    private final MessageTokenService messageTokenService;
    private final MessageService<AssetDepreciationDTO> assetDepreciationRMSCreate;
    private final MessageService<AssetDepreciationDTO> assetDepreciationRMSUpdate;
    private final MessageService<Long> assetDepreciationRMSDelete;

    public AssetDepreciationMutationResource(final MessageTokenService messageTokenService, final MessageService<AssetDepreciationDTO> assetDepreciationRMSCreate,
                                             final MessageService<AssetDepreciationDTO> assetDepreciationRMSUpdate, final MessageService<Long> assetDepreciationRMSDelete) {
        this.messageTokenService = messageTokenService;
        this.assetDepreciationRMSCreate = assetDepreciationRMSCreate;
        this.assetDepreciationRMSUpdate = assetDepreciationRMSUpdate;
        this.assetDepreciationRMSDelete = assetDepreciationRMSDelete;
    }

    @Override
    public MessageTokenDTO createAssetAcquisition(final AssetDepreciationDTO assetDepreciationDTO) throws URISyntaxException {
        log.debug("Request to create entity received for action : {} and delegated to al a carte creation api", assetDepreciationDTO);
        MessageTokenDTO tokenForEnqueued = assetDepreciationRMSCreate.sendMessage(assetDepreciationDTO);
        tokenForEnqueued.setContentFullyEnqueued(true);
        return messageTokenService.save(tokenForEnqueued);
    }

    @Override
    public MessageTokenDTO updateAssetAcquisition(final AssetDepreciationDTO assetDepreciationDTO) throws URISyntaxException {
        log.debug("Request to update entity id : {} received and delegated to al a carte update api", assetDepreciationDTO);
        MessageTokenDTO tokenForEnqueued = assetDepreciationRMSUpdate.sendMessage(assetDepreciationDTO);
        tokenForEnqueued.setContentFullyEnqueued(true);
        return messageTokenService.save(tokenForEnqueued);
    }

    @Override
    public MessageTokenDTO deleteEntity(final Long id) {
        log.debug("Request to delete entity id : {} received and delegated to al a carte deletion api", id);
        MessageTokenDTO tokenForEnqueued = assetDepreciationRMSDelete.sendMessage(id);
        tokenForEnqueued.setContentFullyEnqueued(true);
        return messageTokenService.save(tokenForEnqueued);
    }
}
