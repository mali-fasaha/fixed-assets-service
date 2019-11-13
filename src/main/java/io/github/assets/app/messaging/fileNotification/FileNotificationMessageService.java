package io.github.assets.app.messaging.fileNotification;

import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.StringedTokenMessageService;
import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.service.FileTypeService;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.MessageTokenDTO;
import io.github.assets.service.mapper.MessageTokenMapper;
import org.springframework.stereotype.Service;

@Service("fileNotificationMessageService")
public class FileNotificationMessageService extends StringedTokenMessageService implements MessageService<TokenizableMessage<String>> {

    private final FileTypeService fileTypeService;

    public FileNotificationMessageService(final TokenGenerator tokenGenerator, final MessageTokenService messageTokenService, final FileNotificationStreams fileNotificationStreams,
                                          final FileTypeService fileTypeService, final MessageTokenMapper messageTokenMapper) {
        super(tokenGenerator, messageTokenService, fileNotificationStreams.outbound(), messageTokenMapper);
        this.fileTypeService = fileTypeService;
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param fileNotification This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    public MessageTokenDTO sendMessage(final FileNotification fileNotification) {

        MessageTokenDTO messageToken = super.sendMessage(fileNotification);

//        // Add file model type info to the message token
//        messageToken.fileModelType(fileTypeService.findOne(Long.parseLong(fileNotification.getFileId())).get().getFileType());

        messageToken.setReceived(true);
        messageToken.setActioned(true);

        return messageToken;
    }
}
