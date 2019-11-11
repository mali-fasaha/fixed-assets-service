package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.Mapping;
import io.github.assets.service.dto.AssetDepreciationDTO;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

@Component("assetDepreciationMTOMapping")
public class AssetDepreciationMTOMapping implements Mapping<AssetDepreciationDTO, AssetDepreciationMTO> {

    @Override
    public AssetDepreciationDTO toValue1(final AssetDepreciationMTO vs) {
        return null;
    }

    @Override
    public AssetDepreciationMTO toValue2(final AssetDepreciationDTO vs) {
        return null;
    }
}
