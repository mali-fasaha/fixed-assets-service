package io.github.assets.app.messaging;

import io.github.assets.app.messaging.fileNotification.FileNotificationStreams;
import io.github.assets.app.messaging.jsonStrings.JsonStringStreams;
import io.github.assets.app.messaging.platform.MessageService;
import io.github.assets.app.messaging.platform.StringTokenMessageService;
import io.github.assets.app.messaging.platform.TokenizableMessage;
import io.github.assets.app.messaging.sample.GreetingsStreams;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.MessageTokenDTO;
import io.github.assets.service.mapper.MessageTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class MessageServiceContainer {

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private MessageTokenService messageTokenService;
    @Autowired
    private MessageTokenMapper messageTokenMapper;
    @Autowired
    private GreetingsStreams greetingsStreams;
    @Autowired
    private FileNotificationStreams fileNotificationStreams;
    @Autowired
    private JsonStringStreams jsonStringStreams;

    @Transactional
    @Bean("greetingsService")
    public MessageService<TokenizableMessage<String>> greetingsService() {

        return greeting -> {
            MessageService<TokenizableMessage<String>> service =
                new StringTokenMessageService(tokenGenerator, messageTokenService, greetingsStreams.outbound(), messageTokenMapper);
            MessageTokenDTO messageTokenDTO = service.sendMessage(greeting);
            messageTokenDTO.setReceived(true);
            messageTokenDTO.setActioned(true);

            return messageTokenDTO;
        };
    }

    @Bean("jsonStringMessageService")
    public MessageService<TokenizableMessage<String>> jsonStringMessageService() {

        return message -> {
            MessageService<TokenizableMessage<String>> service =
                new StringTokenMessageService(tokenGenerator, messageTokenService, jsonStringStreams.acquisitionsCreateOutbound(), messageTokenMapper);
            MessageTokenDTO messageTokenDTO = service.sendMessage(message);
            messageTokenDTO.setReceived(true);
            messageTokenDTO.setActioned(true);

            return messageTokenDTO;
        };
    }

    @Bean("fileNotificationMessageService")
    public MessageService<TokenizableMessage<String>> fileUploadNotificationMessageService() {

        return new StringTokenMessageService(tokenGenerator, messageTokenService, fileNotificationStreams.outbound(), messageTokenMapper);
    }
}
