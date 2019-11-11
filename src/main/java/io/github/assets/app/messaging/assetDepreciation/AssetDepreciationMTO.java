package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.TokenizableMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AssetDepreciationMTO implements TokenizableMessage<String> {

    private String description;
    private long timestamp;
    private String messageToken;
    private Long id;
    private double depreciationAmount;
    private String depreciationDate;
    private Long categoryId;
    private Long assetItemId;
}
