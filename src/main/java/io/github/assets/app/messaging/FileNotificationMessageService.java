package io.github.assets.app.messaging;

import io.github.assets.app.file.FileNotification;
import io.github.assets.app.file.Tokenizable;
import io.github.assets.domain.MessageToken;

public class FileNotificationMessageService extends DefaultMessageService implements MessageService<Tokenizable> {


    public FileNotificationMessageService(FileNotificationStreams fileNotificationStreams) {
        super(fileNotificationStreams.outboundNotifications());
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param fileNotification This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    public MessageToken sendMessage(final FileNotification fileNotification) {

        return super.sendMessage(fileNotification)
                    .description(fileNotification.getDescription())
                    .tokenValue(fileNotification.getMessageToken())
                    .timeSent(fileNotification.getTimeOfUpload())
                    .description(fileNotification.getFilename());
    }
}
