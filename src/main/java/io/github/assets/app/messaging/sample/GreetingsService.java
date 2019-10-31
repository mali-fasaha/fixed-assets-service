package io.github.assets.app.messaging.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.StringedTokenMessageService;
import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.app.messaging.fileNotification.FileNotificationStreams;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.domain.MessageToken;
import io.github.assets.service.FileTypeService;
import io.github.assets.service.MessageTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
@Slf4j
public class GreetingsService extends StringedTokenMessageService implements MessageService<TokenizableMessage<String>>{

    public GreetingsService(final TokenGenerator tokenGenerator, final MessageTokenService messageTokenService, final GreetingsStreams greetingsStreams) {
        super(tokenGenerator, messageTokenService, greetingsStreams.outbound());
    }

    public MessageToken sendMessage(final Greetings greetings) {

        MessageToken messageToken = super.sendMessage(greetings);

        return messageToken.received(true).actioned(true);
    }
}
