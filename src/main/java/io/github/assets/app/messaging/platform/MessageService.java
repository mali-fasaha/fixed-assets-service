package io.github.assets.app.messaging.platform;

import io.github.assets.domain.MessageToken;
import io.github.assets.service.dto.MessageTokenDTO;

/**
 * This is an abstraction for sending a services into a queue. It is expected that the implemntation
 * internally has a way of persisting message-tokens generated
 *
 * @param <T> Type of services
 */
public interface MessageService<T> {

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param message This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    MessageTokenDTO sendMessage(final T message);
}
