package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.TokenizableMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AssetDepreciationMTO implements TokenizableMessage<String> {

    private String description;
    private long timestamp;
    private String messageToken;
}
