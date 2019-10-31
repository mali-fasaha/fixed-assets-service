package io.github.assets.app.messaging.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.domain.MessageToken;
import io.github.assets.service.MessageTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
@Slf4j
public class GreetingsService implements MessageService<Greetings> {

    private final MessageChannel messageChannel;
    private final TokenGenerator tokenGenerator;
    private final MessageTokenService messageTokenService;

    public GreetingsService(final GreetingsStreams greetingsStreams, final TokenGenerator tokenGenerator, final MessageTokenService messageTokenService) {
        this.messageChannel = greetingsStreams.outboundGreetings();
        this.tokenGenerator = tokenGenerator;
        this.messageTokenService = messageTokenService;
    }

    public MessageToken sendMessage(final Greetings greetings) {
        log.info("Sending greetings...{}", greetings);

        MessageToken messageToken = null;
        try {
            messageToken = new MessageToken()
                .tokenValue(tokenGenerator.md5Digest(greetings))
                .description(greetings.getMessage())
                .timeSent(greetings.getTimestamp());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        messageChannel.send(MessageBuilder.withPayload(greetings).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());

        return messageTokenService.save(messageToken);
    }
}
