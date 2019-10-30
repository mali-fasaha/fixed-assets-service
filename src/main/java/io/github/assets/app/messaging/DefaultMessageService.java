package io.github.assets.app.messaging;

import io.github.assets.app.file.Tokenizable;
import io.github.assets.domain.MessageToken;

public class DefaultMessageService implements MessageService<Tokenizable> {

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param message This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    @Override
    public MessageToken sendMessage(final Tokenizable message) {


        // TODO Send message with producer

        return new MessageToken()
            .tokenValue(message.getMessageToken());
    }
}
