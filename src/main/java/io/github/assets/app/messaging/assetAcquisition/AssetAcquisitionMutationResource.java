package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.MutationResource;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;

@Slf4j
@Transactional
@Service("assetAcquisitionMutationResource")
public class AssetAcquisitionMutationResource implements MutationResource<AssetAcquisitionDTO> {


    private final AssetAcquisitionRMSCreate assetAcquisitionRMSCreate;
    private final AssetAcquisitionRMSUpdate assetAcquisitionRMSUpdate;
    private final AssetAcquisitionRMSDelete assetAcquisitionRMSDelete;
    private final MessageTokenService messageTokenService;

    public AssetAcquisitionMutationResource(final AssetAcquisitionRMSCreate assetAcquisitionRMSCreate, final AssetAcquisitionRMSUpdate assetAcquisitionRMSUpdate,
                                            final AssetAcquisitionRMSDelete assetAcquisitionRMSDelete, final MessageTokenService messageTokenService) {
        this.assetAcquisitionRMSCreate = assetAcquisitionRMSCreate;
        this.assetAcquisitionRMSUpdate = assetAcquisitionRMSUpdate;
        this.assetAcquisitionRMSDelete = assetAcquisitionRMSDelete;
        this.messageTokenService = messageTokenService;
    }

    @Override
    public MessageTokenDTO createEntity(final AssetAcquisitionDTO assetAcquisitionDTO) throws URISyntaxException {
        log.debug("Request to create entity received for action : {} and delegated to al a carte creation api", assetAcquisitionDTO);
        MessageTokenDTO tokenForEnqueued = assetAcquisitionRMSCreate.sendMessage(assetAcquisitionDTO);
        tokenForEnqueued.setContentFullyEnqueued(true);
        return messageTokenService.save(tokenForEnqueued);
    }

    @Override
    public MessageTokenDTO updateEntity(final AssetAcquisitionDTO assetAcquisitionDTO) throws URISyntaxException {
        log.debug("Request to update entity id : {} received and delegated to al a carte update api", assetAcquisitionDTO);
        MessageTokenDTO tokenForEnqueued = assetAcquisitionRMSUpdate.sendMessage(assetAcquisitionDTO);
        tokenForEnqueued.setContentFullyEnqueued(true);
        return messageTokenService.save(tokenForEnqueued);
    }

    @Override
    public MessageTokenDTO deleteEntity(final Long id) {
        log.debug("Request to delete entity id : {} received and delegated to al a carte deletion api", id);
        MessageTokenDTO tokenForEnqueued = assetAcquisitionRMSDelete.sendMessage(id);
        tokenForEnqueued.setContentFullyEnqueued(true);
        return messageTokenService.save(tokenForEnqueued);
    }
}
