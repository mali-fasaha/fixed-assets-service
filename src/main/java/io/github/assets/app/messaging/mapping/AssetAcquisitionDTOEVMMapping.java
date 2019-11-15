package io.github.assets.app.messaging.mapping;

import io.github.assets.app.Mapping;
import io.github.assets.app.model.AssetAcquisitionEVM;
import io.github.assets.service.FixedAssetCategoryService;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import io.github.assets.service.dto.FixedAssetCategoryDTO;
import org.springframework.stereotype.Component;

import static io.github.assets.app.AppConstants.DATETIME_FORMATTER;
import static org.apache.commons.lang3.math.NumberUtils.toDouble;

@Component("assetAcquisitionDTOEVMMapping")
public class AssetAcquisitionDTOEVMMapping implements Mapping<AssetAcquisitionDTO, AssetAcquisitionEVM> {

    // TODO Simplify
    private final FixedAssetCategoryService fixedAssetCategoryService;

    public AssetAcquisitionDTOEVMMapping(final FixedAssetCategoryService fixedAssetCategoryService) {
        this.fixedAssetCategoryService = fixedAssetCategoryService;
    }

    @Override
    public AssetAcquisitionDTO toValue1(final AssetAcquisitionEVM vs) {
        return null;
    }

    @Override
    public AssetAcquisitionEVM toValue2(final AssetAcquisitionDTO vs) {
        FixedAssetCategoryDTO assetCategory =
            fixedAssetCategoryService.findOne(vs.getAssetCategoryId()).orElseThrow(() -> new IllegalArgumentException("Could not find category for ID : " + vs.getAssetCategoryId()));


        return AssetAcquisitionEVM.builder()
                                  .acquisitionMonth(DATETIME_FORMATTER.format(vs.getAcquisitionMonth()))
                                  .assetSerial(vs.getAssetSerial())
                                  .serviceOutletCode(vs.getServiceOutletCode())
                                  .acquisitionTransactionIdNumber(vs.getAcquisitionTransactionId())
                                  // TODO  .acquisitionTransactionDate(DATETIME_FORMATTER.format(vs.getAcquisitionTransactionDate()))
                                  .assetCategory(assetCategory.getCategoryName())
                                  .description(vs.getDescription())
                                  .purchaseAmount(toDouble(vs.getPurchaseAmount()))
                                  // TODO .assetDealerName(vs.getDealerName())
                                  //TODO .assetInvoiceNumber(// TODO)
                                  //TODO .timestamp(// TODO)
                                  //TODO .messageToken(// TODO)
                                  .build();
    }
}
