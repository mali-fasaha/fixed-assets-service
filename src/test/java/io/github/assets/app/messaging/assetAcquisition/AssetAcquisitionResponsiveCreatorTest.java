package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.excel.ExcelDeserializerContainer;
import io.github.assets.app.excel.ExcelFileDeserializer;
import io.github.assets.app.messaging.ResponsiveListener;
import io.github.assets.app.messaging.fileNotification.FileNotification;
import io.github.assets.service.FileUploadService;
import io.github.assets.service.dto.FileUploadDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;

import java.io.IOException;
import java.util.Optional;

import static io.github.assets.app.excel.ExcelTestUtil.readFile;
import static io.github.assets.app.excel.ExcelTestUtil.toBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class AssetAcquisitionResponsiveCreatorTest {

    private ResponsiveListener<FileNotification, Message<String>> assetAcquisitionListener;

    @Mock
    private FileUploadService fileUploadService;

    private ExcelFileDeserializer<AssetAcquisitionMTO> assetAcquisitionMTOExcelFileDeserializer;

    private FileNotification fileNotification;

    private FileUploadDTO fileUploadDTO;
    private FileUploadDTO foundUploadDTO;

    @BeforeEach
    void setUp() throws IOException {

        MockitoAnnotations.initMocks(this);

        fileNotification = FileNotification.builder().fileId("1001").build();
        fileUploadDTO = new FileUploadDTO();
        fileUploadDTO.setId(Long.parseLong("1001"));
        fileUploadDTO.setDataFile(toBytes(readFile("asset_acquisition.xlsx")));

        foundUploadDTO = new FileUploadDTO();
        foundUploadDTO.setDataFile(toBytes(readFile("asset_acquisition.xlsx")));

        when(fileUploadService.findOne(fileUploadDTO.getId())).thenReturn(Optional.of(foundUploadDTO));

        ExcelDeserializerContainer container = new ExcelDeserializerContainer();

        assetAcquisitionListener = new AssetAcquisitionResponsiveCreator(fileUploadService, container.assetAcquisitionEVMExcelFileDeserializer());
    }

    @Test
    void attendMessage() {

        Message<String> sentMessage = assetAcquisitionListener.attendMessage(fileNotification);

        assertThat(sentMessage.getPayload()).isEqualTo("[{\"rowIndex\":1,\"acquisitionMonth\":\"2019/09/01\",\"assetSerial\":\"GLM009827\",\"serviceOutletCode\":\"802\"," +
                                                           "\"acquisitionTransactionDate\":\"2019/09/15\",\"assetCategory\":\"COMPUTERS\",\"description\":\"ACQUISITION OF ASSET 1\"," +
                                                           "\"purchaseAmount\":118285.2,\"timestamp\":0},{\"rowIndex\":2,\"acquisitionMonth\":\"2019/03/02\",\"assetSerial\":\"GLM009828\"," +
                                                           "\"serviceOutletCode\":\"672\",\"acquisitionTransactionDate\":\"2019/06/10\",\"assetCategory\":\"COMPUTER SOFTWARE\"," +
                                                           "\"description\":\"ACQUISITION OF ASSET 2\",\"purchaseAmount\":150800.0,\"timestamp\":0},{\"rowIndex\":3," +
                                                           "\"acquisitionMonth\":\"2019/06/03\",\"assetSerial\":\"GLM009829\",\"serviceOutletCode\":\"344\"," +
                                                           "\"acquisitionTransactionDate\":\"05/06/019\",\"assetCategory\":\"COMPUTER SOFTWARE\",\"description\":\"ACQUISITION OF ASSET 3\"," +
                                                           "\"purchaseAmount\":1261259.0,\"timestamp\":0},{\"rowIndex\":4,\"acquisitionMonth\":\"2019/09/01\",\"assetSerial\":\"GLM009830\"," +
                                                           "\"serviceOutletCode\":\"908\",\"acquisitionTransactionDate\":\"2019/09/05\",\"assetCategory\":\"FURNITURE \\u0026 FIXTURES\"," +
                                                           "\"description\":\"ACQUISITION OF ASSET 4\",\"purchaseAmount\":143000.0,\"timestamp\":0},{\"rowIndex\":5," +
                                                           "\"acquisitionMonth\":\"2019/03/02\",\"assetSerial\":\"GLM009831\",\"serviceOutletCode\":\"739\"," +
                                                           "\"acquisitionTransactionDate\":\"2019/03/12\",\"assetCategory\":\"ELECTRONIC EQUIPMENT\",\"description\":\"ACQUISITION OF ASSET 5\"," +
                                                           "\"purchaseAmount\":93150.0,\"timestamp\":0},{\"rowIndex\":6,\"acquisitionMonth\":\"2019/06/03\",\"assetSerial\":\"GLM009832\"," +
                                                           "\"serviceOutletCode\":\"867\",\"acquisitionTransactionDate\":\"2019/06/01\",\"assetCategory\":\"COMPUTERS\",\"description\":\"ACQUISITION" +
                                                           " OF ASSET 6\",\"purchaseAmount\":173376.0,\"timestamp\":0},{\"rowIndex\":7,\"acquisitionMonth\":\"2019/09/01\"," +
                                                           "\"assetSerial\":\"GLM009833\",\"serviceOutletCode\":\"693\",\"acquisitionTransactionDate\":\"2019/09/25\"," +
                                                           "\"assetCategory\":\"COMPUTERS\",\"description\":\"ACQUISITION OF ASSET 7\",\"purchaseAmount\":110000.0,\"timestamp\":0},{\"rowIndex\":8," +
                                                           "\"acquisitionMonth\":\"2019/03/02\",\"assetSerial\":\"GLM009834\",\"serviceOutletCode\":\"573\"," +
                                                           "\"acquisitionTransactionDate\":\"2019/03/16\",\"assetCategory\":\"CAPITAL WORK IN PROGRESS\",\"description\":\"ACQUISITION OF ASSET 8\"," +
                                                           "\"purchaseAmount\":93240.0,\"timestamp\":0},{\"rowIndex\":9,\"acquisitionMonth\":\"2019/06/03\",\"assetSerial\":\"GLM009835\"," +
                                                           "\"serviceOutletCode\":\"331\",\"acquisitionTransactionDate\":\"2019/06/28\",\"assetCategory\":\"COMPUTERS\",\"description\":\"ACQUISITION" +
                                                           " OF ASSET 9\",\"purchaseAmount\":93240.0,\"timestamp\":0},{\"rowIndex\":10,\"acquisitionMonth\":\"2019/09/01\"," +
                                                           "\"assetSerial\":\"GLM009836\",\"serviceOutletCode\":\"674\",\"acquisitionTransactionDate\":\"2019/09/14\"," +
                                                           "\"assetCategory\":\"COMPUTERS\",\"description\":\"ACQUISITION OF ASSET 10\",\"purchaseAmount\":55000.0,\"timestamp\":0},{\"rowIndex\":11," +
                                                           "\"acquisitionMonth\":\"2019/03/02\",\"assetSerial\":\"GLM009837\",\"serviceOutletCode\":\"331\"," +
                                                           "\"acquisitionTransactionDate\":\"2019/03/26\",\"assetCategory\":\"ELECTRONIC EQUIPMENT\",\"description\":\"ACQUISITION OF ASSET 11\"," +
                                                           "\"purchaseAmount\":93240.0,\"timestamp\":0},{\"rowIndex\":12,\"acquisitionMonth\":\"2019/06/03\",\"assetSerial\":\"GLM009838\"," +
                                                           "\"serviceOutletCode\":\"885\",\"acquisitionTransactionDate\":\"2019/06/13\",\"assetCategory\":\"ELECTRONIC EQUIPMENT\"," +
                                                           "\"description\":\"ACQUISITION OF ASSET 12\",\"purchaseAmount\":99840.0,\"timestamp\":0}]");

        // TODO assertThat(sentMessage.getPayload()).isEqualTo(toJsonString(readFile("asset_acquisition.json")));
    }
}
