package io.github.assets.app.messaging.sample;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Greetings {

    private long timestamp;
    private String message;

    @Override
    public String toString() {
        return message;
    }
}
