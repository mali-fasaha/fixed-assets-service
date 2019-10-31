package io.github.assets.app.messaging.sample;

import io.github.assets.app.messaging.Tokenizable;
import io.github.assets.app.messaging.TokenizableMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Greetings implements TokenizableMessage<String> {

    private long timestamp;
    private String message;
    private String messageToken;
    private String description;

    @Override
    public String toString() {
        return message;
    }
}
