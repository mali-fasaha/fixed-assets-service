package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.StringedTokenMessageService;
import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.domain.MessageToken;
import io.github.assets.service.MessageTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Asset-acquisition-resource-message-service for delete method
 */
@Slf4j
@Transactional
@Service("assetAcquisitionRMSDelete")
public class AssetAcquisitionRMSDelete implements MessageService<Long> {

    private final MessageTokenService messageTokenService;
    private final TokenGenerator tokenGenerator;
    private final AssetAcquisitionResourceStreams assetAcquisitionResourceStreams;

    public AssetAcquisitionRMSDelete(final MessageTokenService messageTokenService, final TokenGenerator tokenGenerator, final AssetAcquisitionResourceStreams assetAcquisitionResourceStreams) {
        this.messageTokenService = messageTokenService;
        this.tokenGenerator = tokenGenerator;
        this.assetAcquisitionResourceStreams = assetAcquisitionResourceStreams;
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param message This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    @Override
    public MessageToken sendMessage(final Long message) {

        MessageService<TokenizableMessage<String>> messageService = new StringedTokenMessageService(tokenGenerator, messageTokenService, assetAcquisitionResourceStreams.outboundDeleteResource());

        return messageService.sendMessage(new TokenizableMessage<String>() {
            private long id;
            private String messageToken;

            @Override
            public String getDescription() {
                return String.valueOf(id);
            }

            @Override
            public long getTimestamp() {
                return System.currentTimeMillis();
            }

            @Override
            public String getMessageToken() {
                return messageToken;
            }

            @Override
            public void setMessageToken(final String messageToken) {
                this.messageToken = messageToken;
            }
        });
    }
}
