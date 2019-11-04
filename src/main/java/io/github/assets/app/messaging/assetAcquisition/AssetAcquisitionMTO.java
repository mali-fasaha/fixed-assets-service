package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.TokenizableMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AssetAcquisitionMTO implements TokenizableMessage<String> {

    private Long id;

    private String description;

    private String acquisitionMonth;

    @NotNull
    private String assetSerial;

    @NotNull
    private String serviceOutletCode;

    @NotNull
    private Long acquisitionTransactionId;

    @NotNull
    private Long assetCategoryId;

    @NotNull
    private double purchaseAmount;

    private Long assetDealerId;

    private Long assetInvoiceId;

    private long timestamp;
    private String messageToken;

}
