package io.github.assets.app.messaging.fileNotification;

import io.github.assets.app.excel.ExcelFileDeserializer;
import io.github.assets.app.messaging.GsonUtils;
import io.github.assets.app.messaging.platform.MessageService;
import io.github.assets.app.messaging.platform.MuteListener;
import io.github.assets.app.messaging.platform.TokenizableMessage;
import io.github.assets.app.messaging.jsonStrings.StringMessageDTO;
import io.github.assets.app.model.AssetAcquisitionEVM;
import io.github.assets.service.FileUploadService;
import io.github.assets.service.dto.FileUploadDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class FileNotificationListener implements MuteListener<FileNotification> {

    private final MessageService<TokenizableMessage<String>> jsonStringMessageService;
    private final FileUploadService fileUploadService;
    private final ExcelFileDeserializer<AssetAcquisitionEVM> deserializer;

    public FileNotificationListener(final MessageService<TokenizableMessage<String>> jsonStringMessageService, final FileUploadService fileUploadService,
                                    final ExcelFileDeserializer<AssetAcquisitionEVM> deserializer) {
        this.jsonStringMessageService = jsonStringMessageService;
        this.fileUploadService = fileUploadService;
        this.deserializer = deserializer;
    }

    @StreamListener(FileNotificationStreams.INPUT)
    public void handleMessage(@Payload FileNotification fileNotification) {
        log.info("File notification received : {}", fileNotification);

        FileUploadDTO fileUpload = fileUploadService.findOne(Long.parseLong(fileNotification.getFileId())).orElseThrow(() -> new IllegalArgumentException("Id # : " + fileNotification.getFileId() +
                                                                                                                                                              " does not exist"));
        String json = GsonUtils.toJsonString(deserializer.deserialize(fileUpload.getDataFile()));

        // ! Sending message to queue
        jsonStringMessageService.sendMessage(StringMessageDTO.builder()
                                                             .jsonString(json)
                                                             .description(fileNotification.getDescription())
                                                             .timestamp(fileNotification.getTimestamp())
                                                             .build());
    }
}
