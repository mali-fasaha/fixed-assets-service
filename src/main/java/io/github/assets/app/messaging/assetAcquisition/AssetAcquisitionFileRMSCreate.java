package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.excel.ExcelFileDeserializer;
import io.github.assets.app.messaging.GsonUtils;
import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.MuteListener;
import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.app.messaging.fileNotification.FileNotification;
import io.github.assets.app.messaging.fileNotification.FileNotificationStreams;
import io.github.assets.app.messaging.jsonStrings.StringMessageDTO;
import io.github.assets.app.model.AssetAcquisitionEVM;
import io.github.assets.service.FileUploadService;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import io.github.assets.service.dto.FileUploadDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

/**
 * This is intended to listen on FileUploads for which relate to asset acquisitions
 * and to deserialize that data and send it back to stream.
 *
 * TODO Deprecate this class
 */
@Deprecated
@Slf4j
@Component("assetAcquisitionResponsiveCreator")
public class AssetAcquisitionFileRMSCreate implements MuteListener<FileNotification> {

    private final MessageService<TokenizableMessage<String>> jsonStringMessageService;
    private final FileUploadService fileUploadService;
    private final ExcelFileDeserializer<AssetAcquisitionEVM> deserializer;

    public AssetAcquisitionFileRMSCreate(final MessageService<TokenizableMessage<String>> jsonStringMessageService, final FileUploadService fileUploadService,
                                         final ExcelFileDeserializer<AssetAcquisitionEVM> deserializer) {
        this.jsonStringMessageService = jsonStringMessageService;
        this.fileUploadService = fileUploadService;
        this.deserializer = deserializer;
    }

    @Override
    @StreamListener(FileNotificationStreams.INPUT)
    public void handleMessage(@Payload FileNotification fileNotification) {
        FileUploadDTO fileUpload = fileUploadService.findOne(Long.parseLong(fileNotification.getFileId())).orElseThrow(() -> new IllegalArgumentException("Id # : " + fileNotification.getFileId() +
                                                                                                                                                              " does not exist"));
        String json = GsonUtils.toJsonString(deserializer.deserialize(fileUpload.getDataFile()));
         MessageBuilder.withPayload(json).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build();

         // ! Sending message to queue
         jsonStringMessageService.sendMessage(StringMessageDTO.builder()
                                                             .jsonString(json)
                                                             .description(fileNotification.getDescription())
                                                             .timestamp(fileNotification.getTimestamp())
                                                             .build());
    }
}
