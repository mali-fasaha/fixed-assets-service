package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.MutationResource;
import io.github.assets.domain.MessageToken;
import io.github.assets.service.dto.AssetAcquisitionDTO;
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

    public AssetAcquisitionMutationResource(final AssetAcquisitionRMSCreate assetAcquisitionRMSCreate, final AssetAcquisitionRMSUpdate assetAcquisitionRMSUpdate,
                                            final AssetAcquisitionRMSDelete assetAcquisitionRMSDelete) {
        this.assetAcquisitionRMSCreate = assetAcquisitionRMSCreate;
        this.assetAcquisitionRMSUpdate = assetAcquisitionRMSUpdate;
        this.assetAcquisitionRMSDelete = assetAcquisitionRMSDelete;
    }

    @Override
    public MessageToken createAssetAcquisition(final AssetAcquisitionDTO assetAcquisitionDTO) throws URISyntaxException {
        log.debug("Request to create entity received for action : {} and delegated to al a carte creation api", assetAcquisitionDTO);
        return assetAcquisitionRMSCreate.sendMessage(assetAcquisitionDTO);
    }

    @Override
    public MessageToken updateAssetAcquisition(final AssetAcquisitionDTO assetAcquisitionDTO) throws URISyntaxException {
        log.debug("Request to update entity id : {} received and delegated to al a carte update api", assetAcquisitionDTO);
        return assetAcquisitionRMSUpdate.sendMessage(assetAcquisitionDTO);
    }

    @Override
    public MessageToken deleteEntity(final Long id) {
        log.debug("Request to delete entity id : {} received and delegated to al a carte deletion api", id);
        return assetAcquisitionRMSDelete.sendMessage(id);
    }
}
