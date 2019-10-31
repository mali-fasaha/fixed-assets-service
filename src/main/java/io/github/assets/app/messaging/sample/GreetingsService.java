package io.github.assets.app.messaging.sample;

import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.StringedTokenMessageService;
import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.domain.MessageToken;
import io.github.assets.service.MessageTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service("greetingsService")
@Slf4j
public class GreetingsService extends StringedTokenMessageService implements MessageService<TokenizableMessage<String>> {

    public GreetingsService(final TokenGenerator tokenGenerator, final MessageTokenService messageTokenService, final GreetingsStreams greetingsStreams) {
        super(tokenGenerator, messageTokenService, greetingsStreams.outbound());
    }

    public MessageToken sendMessage(final Greetings greetings) {
        return super.sendMessage(greetings).received(true).actioned(true);
    }
}
