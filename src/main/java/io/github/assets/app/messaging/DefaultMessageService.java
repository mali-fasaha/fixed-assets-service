package io.github.assets.app.messaging;

import io.github.assets.app.file.Tokenizable;
import io.github.assets.domain.MessageToken;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

public class DefaultMessageService implements MessageService<Tokenizable> {

    private final MessageChannel messageChannel;

    public DefaultMessageService(final MessageChannel messageChannel) {
        this.messageChannel = messageChannel;
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param message This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    @Override
    public MessageToken sendMessage(final Tokenizable message) {

        messageChannel.send(MessageBuilder.withPayload(message).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());

        return new MessageToken()
            .tokenValue(message.getMessageToken());
    }
}
