package io.github.assets.app.messaging.mapping;

import io.github.assets.app.Mapping;
import io.github.assets.app.model.AssetAcquisitionEVM;
import io.github.assets.service.AssetAcquisitionQueryService;
import io.github.assets.service.AssetAcquisitionService;
import io.github.assets.service.AssetTransactionQueryService;
import io.github.assets.service.AssetTransactionService;
import io.github.assets.service.DealerService;
import io.github.assets.service.FixedAssetCategoryService;
import io.github.assets.service.FixedAssetInvoiceService;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import io.github.assets.service.dto.AssetTransactionCriteria;
import io.github.assets.service.dto.AssetTransactionDTO;
import io.github.assets.service.dto.DealerDTO;
import io.github.assets.service.dto.FixedAssetCategoryDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static io.github.assets.app.AppConstants.DATETIME_FORMATTER;
import static org.apache.commons.lang3.math.NumberUtils.toDouble;

@Service("assetAcquisitionDTOEVMMapping")
public class AssetAcquisitionDTOEVMMapping implements Mapping<AssetAcquisitionDTO, AssetAcquisitionEVM> {

    // TODO Simplify
    private AssetAcquisitionQueryService assetAcquisitionQueryService;
    private AssetTransactionQueryService assetTransactionQueryService;
    private AssetAcquisitionService assetAcquisitionService;
    private AssetTransactionService assetTransactionService;
    private FixedAssetCategoryService fixedAssetCategoryService;
    private FixedAssetInvoiceService fixedAssetInvoiceService;
    private DealerService dealerService;

    public AssetAcquisitionDTOEVMMapping(final AssetAcquisitionQueryService assetAcquisitionQueryService, final AssetTransactionQueryService assetTransactionQueryService,
                                         final AssetAcquisitionService assetAcquisitionService, final AssetTransactionService assetTransactionService,
                                         final FixedAssetCategoryService fixedAssetCategoryService, final FixedAssetInvoiceService fixedAssetInvoiceService, final DealerService dealerService) {
        this.assetAcquisitionQueryService = assetAcquisitionQueryService;
        this.assetTransactionQueryService = assetTransactionQueryService;
        this.assetAcquisitionService = assetAcquisitionService;
        this.assetTransactionService = assetTransactionService;
        this.fixedAssetCategoryService = fixedAssetCategoryService;
        this.fixedAssetInvoiceService = fixedAssetInvoiceService;
        this.dealerService = dealerService;
    }

    @Override
    public AssetAcquisitionDTO toValue1(final AssetAcquisitionEVM vs) {

        String transactionID = vs.getAcquisitionTransactionIdNumber();

        AssetTransactionCriteria assetTransactionCriteria = new AssetTransactionCriteria();

        AssetTransactionDTO assetTransaction = assetTransactionQueryService.findByCriteria(assetTransactionCriteria, Pageable.unpaged())
                                                                           .get()
                                                                           .findFirst()
                                                                           .orElseThrow(
                                                                               () -> new IllegalArgumentException("Could not find transaction for id " + vs.getAcquisitionTransactionIdNumber()));
        return null;
    }

    @Override
    public AssetAcquisitionEVM toValue2(final AssetAcquisitionDTO vs) {
        AssetTransactionDTO assetTransaction = assetTransactionService.findOne(vs.getAcquisitionTransactionId())
                                                                      .orElseThrow(() -> new IllegalArgumentException("Could not find transaction for ID : " + vs.getAcquisitionTransactionId()));
        FixedAssetCategoryDTO assetCategory =
            fixedAssetCategoryService.findOne(vs.getAssetCategoryId()).orElseThrow(() -> new IllegalArgumentException("Could not find category for ID : " + vs.getAssetCategoryId()));

        DealerDTO assetDealer = dealerService.findOne(vs.getAssetDealerId()).orElseThrow(() -> new IllegalArgumentException("Could not find Dealer for ID : " + vs.getAssetDealerId()));

        return AssetAcquisitionEVM.builder()
                                  .acquisitionMonth(DATETIME_FORMATTER.format(vs.getAcquisitionMonth()))
                                  .assetSerial(vs.getAssetSerial())
                                  .serviceOutletCode(vs.getServiceOutletCode())
                                  .acquisitionTransactionIdNumber(assetTransaction.getTransactionReference())
                                  .acquisitionTransactionDate(DATETIME_FORMATTER.format(assetTransaction.getTransactionDate()))
                                  .assetCategory(assetCategory.getCategoryName())
                                  .description(vs.getDescription())
                                  .purchaseAmount(toDouble(vs.getPurchaseAmount()))
                                  .assetDealerName(assetDealer.getDealerName())
                                  //TODO .assetInvoiceNumber(// TODO)
                                  //TODO .timestamp(// TODO)
                                  //TODO .messageToken(// TODO)
                                  .build();
    }
}
