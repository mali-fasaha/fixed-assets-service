package io.github.assets.app.excel;

import io.github.assets.app.excel.deserializer.DefaultExcelFileDeserializer;
import io.github.assets.app.messaging.assetAcquisition.AssetAcquisitionMTO;
import io.github.assets.app.model.AssetAcquisitionEVM;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static io.github.assets.app.excel.PoijiOptionsConfig.getDefaultPoijiOptions;

@Configuration
public class ExcelDeserializerContainer {

    // TODO Containerize defaultExcelDeserializers
    @Bean("assetAcquisitionExcelFileDeserializer")
    public ExcelFileDeserializer<AssetAcquisitionEVM> assetAcquisitionExcelFileDeserializer() {

        return excelFile -> {
            DefaultExcelFileDeserializer<AssetAcquisitionEVM> deserializer =
                new DefaultExcelFileDeserializer<>(AssetAcquisitionEVM.class, getDefaultPoijiOptions());
            return deserializer.deserialize(excelFile);
        };
    }
}
