package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.Mapping;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static io.github.assets.app.AppConstants.DATETIME_FORMATTER;
import static org.apache.commons.lang3.math.NumberUtils.toDouble;
import static org.apache.commons.lang3.math.NumberUtils.toScaledBigDecimal;

@Component("assetAcquisitionMTOMapper")
public class AssetAcquisitionMTOMapper implements Mapping<AssetAcquisitionDTO, AssetAcquisitionMTO> {

    @Override
    public AssetAcquisitionDTO toValue1(final AssetAcquisitionMTO vs) {
        return AssetAcquisitionDTO.builder()
            .id(vs.getId())
            .description(vs.getDescription())
            .acquisitionMonth(LocalDate.parse(vs.getAcquisitionMonth(), DATETIME_FORMATTER))
            .assetSerial(vs.getAssetSerial())
            .serviceOutletCode(vs.getServiceOutletCode())
            .acquisitionTransactionId(vs.getAcquisitionTransactionId())
            .assetCategoryId(vs.getAssetCategoryId())
            .purchaseAmount(toScaledBigDecimal(vs.getPurchaseAmount()))
            .assetDealerId(vs.getAssetDealerId())
            .assetInvoiceId(vs.getAssetInvoiceId())
        // TODO confirm effects of this mutation
        // TODO .timestamp(System.currentTimeMillis())
        // TODO .messageToken(vs.getMessageToken)
        .build();
    }

    @Override
    public AssetAcquisitionMTO toValue2(final AssetAcquisitionDTO vs) {
        return AssetAcquisitionMTO.builder()
                                  .id(vs.getId())
                                  .description(vs.getDescription())
                                  .acquisitionMonth(DATETIME_FORMATTER.format(vs.getAcquisitionMonth()))
                                  .assetSerial(vs.getAssetSerial())
                                  .serviceOutletCode(vs.getServiceOutletCode())
                                  .acquisitionTransactionId(vs.getAcquisitionTransactionId())
                                  .assetCategoryId(vs.getAssetCategoryId())
                                  .purchaseAmount(toDouble(vs.getPurchaseAmount()))
                                  .assetDealerId(vs.getAssetDealerId())
                                  .assetInvoiceId(vs.getAssetInvoiceId())
                                  // TODO confirm effects of this mutation
                                  // TODO .timestamp(System.currentTimeMillis())
                                  // TODO .messageToken(vs.getMessageToken)
                                  .build();
    }
}
