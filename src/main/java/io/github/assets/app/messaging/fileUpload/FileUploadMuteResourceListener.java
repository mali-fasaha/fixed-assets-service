package io.github.assets.app.messaging.fileUpload;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.EntityResource;
import io.github.assets.app.messaging.MuteResourceListener;
import io.github.assets.app.messaging.ResponsiveEntityResource;
import io.github.assets.app.messaging.TokenValueSearch;
import io.github.assets.service.dto.FileUploadDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.net.URISyntaxException;

import static io.github.assets.app.messaging.fileUpload.FileUploadResourceStreams.CREATE_RESOURCE_IN;
import static io.github.assets.app.messaging.fileUpload.FileUploadResourceStreams.DELETE_RESOURCES_IN;
import static io.github.assets.app.messaging.fileUpload.FileUploadResourceStreams.UPDATE_RESOURCES_IN;

@Slf4j
@Transactional
@Service("fileUploadMuteResourceListener")
public class FileUploadMuteResourceListener implements MuteResourceListener<FileUploadMTO> {

    private final TokenValueSearch<String> stringTokenValueSearch;
    private final EntityResource<FileUploadMTO, DeleteMessageDTO> assetEntityResource;

    private final ResponsiveEntityResource<FileUploadMTO, DeleteMessageDTO, FileUploadDTO> fileUploadResponsiveEntityResource;

    public FileUploadMuteResourceListener(final TokenValueSearch<String> stringTokenValueSearch, final EntityResource<FileUploadMTO, DeleteMessageDTO> fileUploadEntityResource,
                                          final ResponsiveEntityResource<FileUploadMTO, DeleteMessageDTO, FileUploadDTO> fileUploadResponsiveEntityResource) {
        this.stringTokenValueSearch = stringTokenValueSearch;
        this.assetEntityResource = fileUploadEntityResource;
        this.fileUploadResponsiveEntityResource = fileUploadResponsiveEntityResource;
    }

    @Override
    @StreamListener(CREATE_RESOURCE_IN)
    public void createEntity(@Payload FileUploadMTO mto) throws URISyntaxException {

        log.debug("Resource DTO received for action : {}", mto);

        MessageTokenDTO messageToken = stringTokenValueSearch.getMessageToken(mto.getMessageToken());

        messageToken.setReceived(true);

        ResponseEntity<FileUploadDTO> fileUploadReponseEntity = fileUploadResponsiveEntityResource.createEntityAndRespond(mto, messageToken);

        // TODO trigger file upload notifications
//        fileNotificationMessageService.notifyCreators(FileNotification.builder()
//                                                                      .messageToken(messageToken.getTokenValue())
//                                                                      .timestamp(messageToken.getTimeSent())
//                                                                      .fileId(String.valueOf(Objects.requireNonNull(fileUploadReponseEntity.getBody()).getId()))
//                                                                      .filename(String.valueOf(Objects.requireNonNull(fileUploadReponseEntity.getBody()).getFileName()))
//                                                                      .build());
    }

    @Override
    @StreamListener(UPDATE_RESOURCES_IN)
    public void updateEntity(@Payload FileUploadMTO mto) throws URISyntaxException {

        log.debug("Resource DTO received for action : {}", mto);

        MessageTokenDTO messageToken = stringTokenValueSearch.getMessageToken(mto.getMessageToken());

        messageToken.setReceived(true);

        assetEntityResource.updateEntity(mto, messageToken);

        // TODO trigger file upload notifications
    }

    @Override
    @StreamListener(DELETE_RESOURCES_IN)
    public void deleteEntity(@Payload DeleteMessageDTO deleteMessageDTO) {

        log.debug("Resource DTO received for action : {}", deleteMessageDTO);

        MessageTokenDTO messageToken = stringTokenValueSearch.getMessageToken(deleteMessageDTO.getMessageToken());

        messageToken.setReceived(true);

        assetEntityResource.deleteEntity(deleteMessageDTO, messageToken);

        // TODO trigger file upload data deletions
    }
}
