package io.github.assets.app.messaging.sample;

import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.StringedTokenMessageService;
import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.MessageTokenDTO;
import io.github.assets.service.mapper.MessageTokenMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service("greetingsService")
@Slf4j
public class GreetingsService extends StringedTokenMessageService implements MessageService<TokenizableMessage<String>> {

    public GreetingsService(final TokenGenerator tokenGenerator, final MessageTokenService messageTokenService, final GreetingsStreams greetingsStreams, final MessageTokenMapper messageTokenMapper) {
        super(tokenGenerator, messageTokenService, greetingsStreams.outbound(), messageTokenMapper);
    }

    public MessageTokenDTO sendMessage(final Greetings greetings) {
        MessageTokenDTO messageTokenDTO = super.sendMessage(greetings);
        messageTokenDTO.setReceived(true);
        messageTokenDTO.setActioned(true);
        return messageTokenDTO;
    }
}
