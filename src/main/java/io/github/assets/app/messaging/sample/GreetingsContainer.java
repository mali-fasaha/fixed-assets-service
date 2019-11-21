package io.github.assets.app.messaging.sample;

import io.github.assets.app.messaging.fileNotification.FileNotificationStreams;
import io.github.assets.app.messaging.jsonStrings.JsonStringStreams;
import io.github.assets.app.messaging.platform.MessageService;
import io.github.assets.app.messaging.platform.StringTokenMessageService;
import io.github.assets.app.messaging.platform.TokenizableMessage;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.MessageTokenDTO;
import io.github.assets.service.mapper.MessageTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GreetingsContainer {

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private MessageTokenService messageTokenService;
    @Autowired
    private MessageTokenMapper messageTokenMapper;
    @Autowired
    private GreetingsStreams greetingsStreams;

    @Bean("greetingsService")
    public MessageService<TokenizableMessage<String>, MessageTokenDTO> greetingsService() {

        return greeting -> {
            MessageService<TokenizableMessage<String>, MessageTokenDTO> service =
                new StringTokenMessageService(tokenGenerator, messageTokenService, greetingsStreams.outbound(), messageTokenMapper);
            MessageTokenDTO messageTokenDTO = service.sendMessage(greeting);
            messageTokenDTO.setReceived(true);
            messageTokenDTO.setActioned(true);

            return messageTokenDTO;
        };
    }
}
