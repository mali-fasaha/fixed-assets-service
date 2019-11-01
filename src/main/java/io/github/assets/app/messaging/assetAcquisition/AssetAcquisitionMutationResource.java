package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.MutationResource;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;

@Transactional
@Service("assetAcquisitionMutationResource")
public class AssetAcquisitionMutationResource implements MutationResource<AssetAcquisitionDTO> {

    // TODO Add message service here

    @Override
    public ResponseEntity<AssetAcquisitionDTO> createAssetAcquisition(final AssetAcquisitionDTO assetAcquisitionDTO) throws URISyntaxException {
        return null;
    }

    @Override
    public ResponseEntity<AssetAcquisitionDTO> updateAssetAcquisition(final AssetAcquisitionDTO assetAcquisitionDTO) throws URISyntaxException {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteEntity(final Long id) {
        return null;
    }
}
