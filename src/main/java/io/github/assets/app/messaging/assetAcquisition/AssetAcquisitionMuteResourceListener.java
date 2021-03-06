package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.EntityResource;
import io.github.assets.app.messaging.MuteResourceListener;
import io.github.assets.app.messaging.TokenValueSearch;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.net.URISyntaxException;

@Slf4j
@Service("assetAcquisitionMuteResourceListener")
@Transactional
public class AssetAcquisitionMuteResourceListener implements MuteResourceListener<AssetAcquisitionMTO> {

    private final TokenValueSearch<String> stringTokenValueSearch;
    private final EntityResource<AssetAcquisitionMTO, DeleteMessageDTO> assetAcquisitionEntityResource;

    public AssetAcquisitionMuteResourceListener(final TokenValueSearch<String> stringTokenValueSearch, final EntityResource<AssetAcquisitionMTO, DeleteMessageDTO> assetAcquisitionEntityResource) {
        this.stringTokenValueSearch = stringTokenValueSearch;
        this.assetAcquisitionEntityResource = assetAcquisitionEntityResource;
    }

    @Override
    @StreamListener(AssetAcquisitionResourceStreams.CREATE_RESOURCE_IN)
    public void createEntity(@Payload AssetAcquisitionMTO assetAcquisitionMTO) throws URISyntaxException {

        log.debug("Resource DTO received for action : {}", assetAcquisitionMTO);

        MessageTokenDTO messageToken = stringTokenValueSearch.getMessageToken(assetAcquisitionMTO.getMessageToken());

        messageToken.setReceived(true);

        assetAcquisitionEntityResource.createEntity(assetAcquisitionMTO, messageToken);
    }

    @Override
    @StreamListener(AssetAcquisitionResourceStreams.UPDATE_RESOURCES_IN)
    public void updateEntity(@Payload AssetAcquisitionMTO assetAcquisitionMTO) throws URISyntaxException {

        log.debug("Resource DTO received for action : {}", assetAcquisitionMTO);

        MessageTokenDTO messageToken = stringTokenValueSearch.getMessageToken(assetAcquisitionMTO.getMessageToken());

        messageToken.setReceived(true);

        assetAcquisitionEntityResource.updateEntity(assetAcquisitionMTO, messageToken);
    }

    @Override
    @StreamListener(AssetAcquisitionResourceStreams.DELETE_RESOURCES_IN)
    public void deleteEntity(@Payload DeleteMessageDTO deleteMessageDTO) {

        log.debug("Resource DTO received for action : {}", deleteMessageDTO);

        MessageTokenDTO messageToken = stringTokenValueSearch.getMessageToken(deleteMessageDTO.getMessageToken());

        messageToken.setReceived(true);

        assetAcquisitionEntityResource.deleteEntity(deleteMessageDTO, messageToken);
    }
}
