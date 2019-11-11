package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.MutationResource;
import io.github.assets.service.dto.AssetDepreciationDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;

@Slf4j
@Transactional
@Service("assetDepreciationMutationResourcee")
public class AssetDepreciationMutationResource implements MutationResource<AssetDepreciationDTO> {

    @Override
    public MessageTokenDTO createAssetAcquisition(final AssetDepreciationDTO assetDepreciationDTO) throws URISyntaxException {
        return null;
    }

    @Override
    public MessageTokenDTO updateAssetAcquisition(final AssetDepreciationDTO assetDepreciationDTO) throws URISyntaxException {
        return null;
    }

    @Override
    public MessageTokenDTO deleteEntity(final Long id) {
        return null;
    }
}
