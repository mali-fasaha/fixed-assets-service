package io.github.assets.app.messaging.fileNotification;

import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.domain.MessageToken;

public interface FileNotificationMessages {

    MessageToken notifyCreators(TokenizableMessage<String> fileNotification);

    MessageToken notifyUpdaters(TokenizableMessage<String> fileNotification);

    MessageToken notifyDeleters(TokenizableMessage<String> fileNotification);
}
