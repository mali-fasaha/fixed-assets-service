package io.github.assets.app.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.domain.MessageToken;
import io.github.assets.service.MessageTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

/**
 * This implementation handles sending of tokenizable messages and generating persisted tokens on the
 * same. It is not managed by the spring  container but rather is extended by client implementations
 * which themselves implement @{code MessageService<TokenizableMessage<String>>} interface
 */
@Slf4j
public class StringedTokenMessageService implements MessageService<TokenizableMessage<String>> {

    private final TokenGenerator tokenGenerator;
    private final MessageTokenService messageTokenService;
    private final MessageChannel messageChannel;

    public StringedTokenMessageService(final TokenGenerator tokenGenerator, final MessageTokenService messageTokenService, final MessageChannel messageChannel) {
        this.tokenGenerator = tokenGenerator;
        this.messageTokenService = messageTokenService;
        this.messageChannel = messageChannel;
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param message This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    @Override
    public MessageToken sendMessage(final TokenizableMessage<String> message) {

        MessageToken messageToken = null;
        try {
            messageToken = new MessageToken().tokenValue(tokenGenerator.md5Digest(message)).description(message.getDescription()).timeSent(message.getTimestamp());
        } catch (JsonProcessingException e) {
            log.error("The service has failed to create a message-token and has been aborted : ", e);
        }

        messageChannel.send(MessageBuilder.withPayload(message).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());

        return messageTokenService.save(messageToken);
    }
}
