package io.github.assets.app.messaging.platform;

import io.github.assets.service.dto.MessageTokenDTO;

/**
 * To search for Message-Token entity with a certain message-token-value
 * @param <T>
 */
public interface TokenValueSearch<T> {

    MessageTokenDTO getMessageToken(final T tokenValue);
}
