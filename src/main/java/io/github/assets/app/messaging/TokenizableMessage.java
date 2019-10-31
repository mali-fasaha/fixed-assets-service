package io.github.assets.app.messaging;

/**
 * This is a message that is tokenizable that contains description and timestamp as well
 *
 * @param <T>
 */
public interface TokenizableMessage<T> extends Tokenizable<T> {

    String getDescription();

    long getTimestamp();
}
