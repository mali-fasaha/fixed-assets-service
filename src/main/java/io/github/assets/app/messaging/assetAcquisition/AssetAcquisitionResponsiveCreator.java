package io.github.assets.app.messaging.assetAcquisition;

import com.google.gson.Gson;
import io.github.assets.app.excel.ExcelFileDeserializer;
import io.github.assets.app.messaging.GsonUtils;
import io.github.assets.app.messaging.ResponsiveListener;
import io.github.assets.app.messaging.fileNotification.FileNotification;
import io.github.assets.app.messaging.fileNotification.FileNotificationStreams;
import io.github.assets.app.model.AssetAcquisitionEVM;
import io.github.assets.service.FileUploadService;
import io.github.assets.service.dto.FileUploadDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

/**
 * This is intended to listen on FileUploads for which relate to asset acquisitions
 * and to deserialize that data and send it back to stream.
 */
@Slf4j
@Component("assetAcquisitionResponsiveCreator")
public class AssetAcquisitionResponsiveCreator implements ResponsiveListener<FileNotification, Message<String>> {

    private final FileUploadService fileUploadService;
    private final ExcelFileDeserializer<AssetAcquisitionEVM> assetAcquisitionassetAcquisitionEVMExcelFileDeserializer;

    public AssetAcquisitionResponsiveCreator(final FileUploadService fileUploadService, final ExcelFileDeserializer<AssetAcquisitionEVM> assetAcquisitionMTOExcelFileDeserializer) {
        this.fileUploadService = fileUploadService;
        this.assetAcquisitionassetAcquisitionEVMExcelFileDeserializer = assetAcquisitionMTOExcelFileDeserializer;
    }

    @Override
    @StreamListener(FileNotificationStreams.INPUT)
    @SendTo(AssetAcquisitionResourceStreams.FILED_CREATE_RESOURCE_IN)
    public Message<String> attendMessage(@Payload FileNotification fileNotification) {
        FileUploadDTO fileUpload = fileUploadService.findOne(Long.parseLong(fileNotification.getFileId())).orElseThrow(() -> new IllegalArgumentException("Id # : " + fileNotification.getFileId() +
                                                                                                                                                              " does not exist"));
        String json = GsonUtils.toJsonString(assetAcquisitionassetAcquisitionEVMExcelFileDeserializer.deserialize(fileUpload.getDataFile()));

        return MessageBuilder.withPayload(json).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build();
    }
}
