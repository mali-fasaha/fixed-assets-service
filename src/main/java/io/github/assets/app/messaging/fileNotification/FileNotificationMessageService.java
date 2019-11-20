package io.github.assets.app.messaging.fileNotification;

import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service("fileNotificationMessageService")
public class FileNotificationMessageService implements MessageService<FileNotification> {

    private final MessageService<TokenizableMessage<String>> fileUploadNotificationMessageService;

    public FileNotificationMessageService(final MessageService<TokenizableMessage<String>> fileUploadNotificationMessageService) {
        this.fileUploadNotificationMessageService = fileUploadNotificationMessageService;
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param fileNotification This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    public MessageTokenDTO sendMessage(final FileNotification fileNotification) {

        log.info("Sending file notification with token# : {}", fileNotification.getMessageToken());

        MessageTokenDTO messageToken = fileUploadNotificationMessageService.sendMessage(fileNotification);
        messageToken.setReceived(true);
        messageToken.setActioned(true);

        return messageToken;
    }
}
