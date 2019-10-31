package io.github.assets.app.messaging;

import java.io.Serializable;

/**
 * This object contains a message token
 * @param <T> Data type for token
 */
public interface Tokenizable<T> extends Serializable {

    T getMessageToken();

    void setMessageToken(T messageToken);
}
