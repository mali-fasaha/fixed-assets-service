package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.TokenizableMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor @ToString
public class AssetAcquisitionMTO implements TokenizableMessage<String> {

    private long timestamp;
    private String description;
    private String messageToken;

}
