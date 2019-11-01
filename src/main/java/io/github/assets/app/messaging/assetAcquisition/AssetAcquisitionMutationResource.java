package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.MutationResource;
import io.github.assets.domain.MessageToken;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;

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
        return assetAcquisitionRMSCreate.sendMessage(assetAcquisitionDTO);
    }

    @Override
    public MessageToken updateAssetAcquisition(final AssetAcquisitionDTO assetAcquisitionDTO) throws URISyntaxException {
        return assetAcquisitionRMSUpdate.sendMessage(assetAcquisitionDTO);
    }

    @Override
    public MessageToken deleteEntity(final Long id) {
        return assetAcquisitionRMSDelete.sendMessage(id);
    }
}
