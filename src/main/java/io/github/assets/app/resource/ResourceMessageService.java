package io.github.assets.app.resource;

import io.github.assets.app.messaging.MessageService;
import io.github.assets.domain.MessageToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service("resourceMessageService")
public class ResourceMessageService extends QueuedResourceMessageService implements MessageService<ResourceMessage<Object>> {

    public ResourceMessageService(final QueuedResourceStreams queuedResourceStreams) {
        super(queuedResourceStreams.outboundQueuedResources());
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param message This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    @Override
    public MessageToken sendMessage(final ResourceMessage<Object> message) {
        return super.sendMessage(message);
    }
}
