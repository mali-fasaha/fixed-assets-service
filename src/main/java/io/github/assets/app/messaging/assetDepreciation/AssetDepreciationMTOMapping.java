package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.Mapping;
import io.github.assets.service.dto.AssetDepreciationDTO;
import org.apache.commons.lang3.math.NumberUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

import static io.github.assets.app.AppConstants.DATETIME_FORMATTER;
import static org.apache.commons.lang3.math.NumberUtils.toDouble;
import static org.apache.commons.lang3.math.NumberUtils.toScaledBigDecimal;

@Component("assetDepreciationMTOMapping")
public class AssetDepreciationMTOMapping implements Mapping<AssetDepreciationDTO, AssetDepreciationMTO> {

    @Override
    public AssetDepreciationDTO toValue1(final AssetDepreciationMTO vs) {
        AssetDepreciationDTO assetDepreciation  = new AssetDepreciationDTO();
        assetDepreciation.setId(vs.getId());
        assetDepreciation.setDescription(vs.getDescription());
        assetDepreciation.setDepreciationAmount(toScaledBigDecimal(vs.getDepreciationAmount()));
        assetDepreciation.setDepreciationDate(LocalDate.parse(vs.getDepreciationDate(), DATETIME_FORMATTER));
        assetDepreciation.setCategoryId(vs.getCategoryId());
        assetDepreciation.setAssetItemId(vs.getAssetItemId());

        return assetDepreciation;
    }

    @Override
    public AssetDepreciationMTO toValue2(final AssetDepreciationDTO vs) {
        return AssetDepreciationMTO.builder()
                                   .description(vs.getDescription())
                                   .id(vs.getId())
                                   .depreciationAmount(toDouble(vs.getDepreciationAmount()))
                                   .depreciationDate(DATETIME_FORMATTER.format(vs.getDepreciationDate()))
                                   .categoryId(vs.getCategoryId())
                                   .assetItemId(vs.getAssetItemId())
                                   .build();
    }
}
