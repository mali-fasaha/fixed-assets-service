package io.github.assets.app.resource;

import io.github.assets.app.messaging.MessageService;
import io.github.assets.domain.MessageToken;
import io.github.assets.domain.enumeration.FileModelType;
import io.github.assets.service.FileTypeService;
import io.github.assets.service.dto.FileUploadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import javax.transaction.Transactional;

// TODO change this message service to be for file uploads
@Transactional
@Service("resourceMessageService")
public class FileUploadResourceMessageService implements MessageService<FileUploadQueueDTO> {

    private final MessageChannel messageChannel;
    @Autowired
    private FileTypeService fileTypeService;


    public FileUploadResourceMessageService(final QueuedResourceStreams queuedResourceStreams) {
        this.messageChannel = queuedResourceStreams.outboundQueuedResources();
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param message This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    @Override
    public MessageToken sendMessage(final FileUploadQueueDTO message) {

        FileModelType fileModelType = fileTypeService.findOne(Long.parseLong(message.getFileTypeId())).get().getFileType();

        if (messageChannel.send(MessageBuilder.withPayload(message).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build())) {

            return new MessageToken()
                .actioned(true)
                .contentFullyEnqueued(true)
                .received(true)
                // TODO replace with token parameters
                .timeSent(System.currentTimeMillis())
                // TODO replace with actual token algorithm
                .tokenValue(String.valueOf(System.currentTimeMillis()))
                .fileModelType(fileModelType)
                .description("DTO successfully transmitted for action for model : " + fileModelType.name());
        }
        return new MessageToken()
            .actioned(false)
            .contentFullyEnqueued(false)
            .received(false)
            // TODO replace with token parameters
            .timeSent(System.currentTimeMillis())
            .tokenValue(String.valueOf(System.currentTimeMillis()))
            // TODO replace with actual token algorithm
            .fileModelType(fileModelType)
            .description("Failed Upload for " + fileModelType.name());
    }
}
