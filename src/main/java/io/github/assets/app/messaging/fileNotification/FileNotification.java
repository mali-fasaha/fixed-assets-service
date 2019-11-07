package io.github.assets.app.messaging.fileNotification;

import io.github.assets.app.messaging.Tokenizable;
import io.github.assets.app.messaging.TokenizableMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FileNotification implements TokenizableMessage<String> {
    private static final long serialVersionUID = -6472961232578342431L;
    private String fileId;

    private long timestamp;

    private String filename;

    private String messageToken;

    private String description;


}
