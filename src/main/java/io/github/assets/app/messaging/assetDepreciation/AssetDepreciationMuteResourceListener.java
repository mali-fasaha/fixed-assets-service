package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.MuteResourceListener;

import java.net.URISyntaxException;

public class AssetDepreciationMuteResourceListener implements MuteResourceListener<AssetDepreciationMTO> {

    @Override
    public void createAssetAcquisition(final AssetDepreciationMTO dto) throws URISyntaxException {

    }

    @Override
    public void updateAssetAcquisition(final AssetDepreciationMTO dto) throws URISyntaxException {

    }

    @Override
    public void deleteEntity(final DeleteMessageDTO deleteMessageDTO) {

    }
}
