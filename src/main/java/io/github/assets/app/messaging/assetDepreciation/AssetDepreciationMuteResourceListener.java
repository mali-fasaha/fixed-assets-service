package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.EntityResource;
import io.github.assets.app.messaging.MuteResourceListener;
import io.github.assets.app.messaging.TokenValueSearch;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URISyntaxException;

import static io.github.assets.app.messaging.assetDepreciation.AssetDepreciationResourceStreams.CREATE_RESOURCE_IN;
import static io.github.assets.app.messaging.assetDepreciation.AssetDepreciationResourceStreams.DELETE_RESOURCES_IN;
import static io.github.assets.app.messaging.assetDepreciation.AssetDepreciationResourceStreams.UPDATE_RESOURCES_IN;

@Slf4j
@Service
@Transactional
public class AssetDepreciationMuteResourceListener implements MuteResourceListener<AssetDepreciationMTO> {

    private final TokenValueSearch<String> stringTokenValueSearch;
    private final EntityResource<AssetDepreciationMTO, DeleteMessageDTO> assetEntityResource;

    public AssetDepreciationMuteResourceListener(final TokenValueSearch<String> stringTokenValueSearch, final EntityResource<AssetDepreciationMTO, DeleteMessageDTO> assetDepreciationEntityResource) {
        this.stringTokenValueSearch = stringTokenValueSearch;
        this.assetEntityResource = assetDepreciationEntityResource;
    }

    @Override
    @StreamListener(CREATE_RESOURCE_IN)
    public void createEntity(@Payload AssetDepreciationMTO mto) throws URISyntaxException {

        log.debug("Resource DTO received for action : {}", mto);

        MessageTokenDTO messageToken = stringTokenValueSearch.getMessageToken(mto.getMessageToken());

        messageToken.setReceived(true);

        assetEntityResource.createEntity(mto, messageToken);
    }

    @Override
    @StreamListener(UPDATE_RESOURCES_IN)
    public void updateEntity(@Payload AssetDepreciationMTO mto) throws URISyntaxException {

        log.debug("Resource DTO received for action : {}", mto);

        MessageTokenDTO messageToken = stringTokenValueSearch.getMessageToken(mto.getMessageToken());

        messageToken.setReceived(true);

        assetEntityResource.updateEntity(mto, messageToken);
    }

    @Override
    @StreamListener(DELETE_RESOURCES_IN)
    public void deleteEntity(@Payload DeleteMessageDTO deleteMessageDTO) {

        log.debug("Resource DTO received for action : {}", deleteMessageDTO);

        MessageTokenDTO messageToken = stringTokenValueSearch.getMessageToken(deleteMessageDTO.getMessageToken());

        messageToken.setReceived(true);

        assetEntityResource.deleteEntity(deleteMessageDTO, messageToken);
    }
}
