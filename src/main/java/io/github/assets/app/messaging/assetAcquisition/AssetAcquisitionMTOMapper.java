package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.Mapping;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import org.springframework.stereotype.Component;

@Component("assetAcquisitionMTOMapper")
public class AssetAcquisitionMTOMapper implements Mapping<AssetAcquisitionDTO, AssetAcquisitionMTO> {

    @Override
    public AssetAcquisitionDTO toValue1(final AssetAcquisitionMTO vs) {
        return null;
    }

    @Override
    public AssetAcquisitionMTO toValue2(final AssetAcquisitionDTO vs) {
        return null;
    }
}
