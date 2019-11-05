package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.FixedAssetServiceApp;
import io.github.assets.app.messaging.MessageService;
import io.github.assets.config.SecurityBeanOverrideConfiguration;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, FixedAssetServiceApp.class})
class AssetAcquisitionMutationResourceIT {

    @Autowired
    private MessageService<AssetAcquisitionDTO> assetAcquisitionRMSCreate;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private AssetAcquisitionResourceStreams assetAcquisitionResourceStreams;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createAssetAcquisitionAPI() {

        long timestamp = System.currentTimeMillis();
        String description = "About the Starks";
        BigDecimal purchaseAmount = BigDecimal.TEN;
        LocalDate acquisitionMonth = LocalDate.of(2019,11,4);
        String serviceOutletCode = "CD";
        long acquisitionTransactionId = 10l;
        long assetDealerId = 30l;
        long assetInvoiceId = 40l;
        long assetCategoryId = 20l;

        AssetAcquisitionDTO assetAcquisitionDTO = new AssetAcquisitionDTO();
        assetAcquisitionDTO.setDescription(description);
        assetAcquisitionDTO.setPurchaseAmount(purchaseAmount);
        assetAcquisitionDTO.setAcquisitionMonth(acquisitionMonth);
        assetAcquisitionDTO.setServiceOutletCode(serviceOutletCode);
        assetAcquisitionDTO.setAcquisitionTransactionId(acquisitionTransactionId);
        assetAcquisitionDTO.setAssetCategoryId(assetCategoryId);
        assetAcquisitionDTO.setAssetDealerId(assetDealerId);
        assetAcquisitionDTO.setAssetInvoiceId(assetInvoiceId);
        assetAcquisitionDTO.setAcquisitionTransactionId(20l);

        assetAcquisitionRMSCreate.sendMessage(assetAcquisitionDTO);

        Object payload = messageCollector.forChannel(assetAcquisitionResourceStreams.outboundCreateResource()).poll().getPayload();

        assertThat(payload.toString()).containsSequence(String.valueOf(timestamp));
        assertThat(payload.toString()).containsSequence(String.valueOf(description));
        assertThat(payload.toString()).containsSequence(String.valueOf(purchaseAmount.toPlainString()));
        assertThat(payload.toString()).containsSequence(String.valueOf(20l));
    }

    @Test
    void updateAssetAcquisition() {
    }

    @Test
    void deleteEntity() {
    }
}
