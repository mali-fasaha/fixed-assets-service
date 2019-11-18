package io.github.assets.app.excel;

import io.github.assets.app.excel.deserializer.DefaultExcelFileDeserializer;
import io.github.assets.app.model.AssetAcquisitionEVM;
import io.github.assets.app.model.AssetTransactionEVM;
import io.github.assets.app.model.DealerEVM;
import io.github.assets.app.model.DepreciationRegimeEVM;
import io.github.assets.app.model.FixedAssetAssessmentEVM;
import io.github.assets.app.model.FixedAssetCategoryEVM;
import io.github.assets.app.model.FixedAssetInvoiceEVM;
import io.github.assets.app.model.FixedAssetItemEVM;
import io.github.assets.app.model.ServiceOutletEVM;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.assets.app.excel.PoijiOptionsConfig.getDefaultPoijiOptions;

@Configuration
public class ExcelDeserializerContainer {

    @Bean("assetAcquisitionExcelFileDeserializer")
    public ExcelFileDeserializer<AssetAcquisitionEVM> assetAcquisitionExcelFileDeserializer() {
        return excelFile -> {
            DefaultExcelFileDeserializer<AssetAcquisitionEVM> deserializer = new DefaultExcelFileDeserializer<>(AssetAcquisitionEVM.class, getDefaultPoijiOptions());
            return deserializer.deserialize(excelFile);
        };
    }

    @Bean("serviceOutletExcelFileDeserializer")
    public ExcelFileDeserializer<ServiceOutletEVM> serviceOutletExcelFileDeserializer() {
        return excelFile -> {
            DefaultExcelFileDeserializer<ServiceOutletEVM> deserializer = new DefaultExcelFileDeserializer<>(ServiceOutletEVM.class, getDefaultPoijiOptions());
            return deserializer.deserialize(excelFile);
        };
    }

    @Bean("assetTransactionExcelFileDeserializer")
    public ExcelFileDeserializer<AssetTransactionEVM> assetTransactionExcelFileDeserializer() {
        return excelFile -> {
            DefaultExcelFileDeserializer<AssetTransactionEVM> deserializer = new DefaultExcelFileDeserializer<>(AssetTransactionEVM.class, getDefaultPoijiOptions());
            return deserializer.deserialize(excelFile);
        };
    }

    @Bean("dealerExcelFileDeserializer")
    public ExcelFileDeserializer<DealerEVM> dealerExcelFileDeserializer() {
        return excelFile -> {
            DefaultExcelFileDeserializer<DealerEVM> deserializer = new DefaultExcelFileDeserializer<>(DealerEVM.class, getDefaultPoijiOptions());
            return deserializer.deserialize(excelFile);
        };
    }

    @Bean("depreciationRegimeExcelFileDeserializer")
    public ExcelFileDeserializer<DepreciationRegimeEVM> depreciationRegimeExcelFileDeserializer() {
        return excelFile -> {
            DefaultExcelFileDeserializer<DepreciationRegimeEVM> deserializer = new DefaultExcelFileDeserializer<>(DepreciationRegimeEVM.class, getDefaultPoijiOptions());
            return deserializer.deserialize(excelFile);
        };
    }

    @Bean("fixedAssetAssessmentExcelFileDeserializer")
    public ExcelFileDeserializer<FixedAssetAssessmentEVM> fixedAssetAssessmentExcelFileDeserializer() {
        return excelFile -> {
            DefaultExcelFileDeserializer<FixedAssetAssessmentEVM> deserializer = new DefaultExcelFileDeserializer<>(FixedAssetAssessmentEVM.class, getDefaultPoijiOptions());
            return deserializer.deserialize(excelFile);
        };
    }

    @Bean("fixedAssetCategoryExcelFileDeserializer")
    public ExcelFileDeserializer<FixedAssetCategoryEVM> fixedAssetCategoryExcelFileDeserializer() {
        return excelFile -> {
            DefaultExcelFileDeserializer<FixedAssetCategoryEVM> deserializer = new DefaultExcelFileDeserializer<>(FixedAssetCategoryEVM.class, getDefaultPoijiOptions());
            return deserializer.deserialize(excelFile);
        };
    }

    @Bean("fixedAssetInvoiceExcelFileDeserializer")
    public ExcelFileDeserializer<FixedAssetInvoiceEVM> fixedAssetInvoiceExcelFileDeserializer() {
        return excelFile -> {
            DefaultExcelFileDeserializer<FixedAssetInvoiceEVM> deserializer = new DefaultExcelFileDeserializer<>(FixedAssetInvoiceEVM.class, getDefaultPoijiOptions());
            return deserializer.deserialize(excelFile);
        };
    }

    @Bean("fixedAssetItemExcelFileDeserializer")
    public ExcelFileDeserializer<FixedAssetItemEVM> fixedAssetItemExcelFileDeserializer() {
        return excelFile -> {
            DefaultExcelFileDeserializer<FixedAssetItemEVM> deserializer = new DefaultExcelFileDeserializer<>(FixedAssetItemEVM.class, getDefaultPoijiOptions());
            return deserializer.deserialize(excelFile);
        };
    }
}
