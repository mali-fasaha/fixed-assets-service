package io.github.assets.app.messaging;

import io.github.assets.app.messaging.fileNotification.FileNotificationStreams;
import io.github.assets.app.messaging.fileUpload.FileUploadResourceStreams;
import io.github.assets.app.messaging.jsonStrings.JsonStringStreams;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.mapper.MessageTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageServiceContainer {

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private MessageTokenService messageTokenService;
    @Autowired
    private MessageTokenMapper messageTokenMapper;
    @Autowired
    private FileUploadResourceStreams fileUploadResourceStreams;
    @Autowired
    private FileNotificationStreams fileNotificationStreams;
    @Autowired
    private JsonStringStreams jsonStringStreams;

    @Bean("jsonStringMessageService")
    public MessageService<TokenizableMessage<String>> jsonStringMessageService() {

        return new StringedTokenMessageService(tokenGenerator, messageTokenService, jsonStringStreams.acquisitionsCreateOutbound(), messageTokenMapper);
    }

    @Bean("fileUploadCreateMessageService")
    public MessageService<TokenizableMessage<String>> fileUploadCreateMessageService() {

        return new StringedTokenMessageService(tokenGenerator, messageTokenService, fileUploadResourceStreams.outboundCreateResource(), messageTokenMapper);
    }

    @Bean("fileUploadUpdateMessageService")
    public MessageService<TokenizableMessage<String>> fileUploadUpdateMessageService() {

        return new StringedTokenMessageService(tokenGenerator, messageTokenService, fileUploadResourceStreams.outboundUpdateResource(), messageTokenMapper);
    }

    @Bean("fileUploadDeleteMessageService")
    public MessageService<TokenizableMessage<String>> fileUploadDeleteMessageService() {

        return new StringedTokenMessageService(tokenGenerator, messageTokenService, fileUploadResourceStreams.outboundDeleteResource(), messageTokenMapper);
    }

    @Bean("fileUploadNotificationMessageService")
    public MessageService<TokenizableMessage<String>> fileUploadNotificationMessageService() {

        return new StringedTokenMessageService(tokenGenerator, messageTokenService, fileNotificationStreams.outbound(), messageTokenMapper);
    }
}
