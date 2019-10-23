package io.github.assets.app.resource;

import io.github.assets.app.messaging.MessageService;
import io.github.assets.domain.MessageToken;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

public class QueuedResourceMessageService implements MessageService<ResourceMessage<Object>> {

    private final MessageChannel messageChannel;

    public QueuedResourceMessageService(final MessageChannel messageChannel) {
        this.messageChannel = messageChannel;
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param message This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    @Override
    public MessageToken sendMessage(final ResourceMessage<Object> message) {

        if (messageChannel.send(MessageBuilder.withPayload(message.getResourceMessage()).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build())) {

            return new MessageToken()
                .actioned(true)
                .contentFullyEnqueued(true)
                .received(true)
                // TODO replace with token parameters
                .timeSent(System.currentTimeMillis())
                // TODO replace with actual token algorithm
                .tokenValue(String.valueOf(System.currentTimeMillis()))
                .fileModelType(message.getFileModelType())
                .description("DTO successfully transmitted for action for model : " + message.getFileModelType().name());
        }
        return new MessageToken()
            .actioned(false)
            .contentFullyEnqueued(false)
            .received(false)
            // TODO replace with token parameters
            .timeSent(System.currentTimeMillis())
            .tokenValue(String.valueOf(System.currentTimeMillis()))
            // TODO replace with actual token algorithm
            .fileModelType(message.getFileModelType())
            .description("Failed Upload for " + message.getFileModelType().name());
    };
}
