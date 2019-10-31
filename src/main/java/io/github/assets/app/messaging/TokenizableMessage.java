package io.github.assets.app.messaging;

public interface TokenizableMessage<T> extends Tokenizable<T> {

    String getDescription();

    long getTimestamp();
}
