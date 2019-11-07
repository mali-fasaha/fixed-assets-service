package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.Mapping;
import io.github.assets.app.messaging.MuteResourceListener;
import io.github.assets.app.messaging.TokenValueSearch;
import io.github.assets.app.resource.decorator.IAssetAcquisitionResource;
import io.github.assets.domain.MessageToken;
import io.github.assets.service.MessageTokenQueryService;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import io.github.assets.service.dto.MessageTokenCriteria;
import io.github.assets.service.dto.MessageTokenDTO;
import io.github.jhipster.service.filter.StringFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.ws.Response;
import java.net.URISyntaxException;

@Slf4j
@Service("assetAcquisitionMuteResourceListener")
@Transactional
public class AssetAcquisitionMuteResourceListener implements MuteResourceListener<AssetAcquisitionMTO> {

    private final IAssetAcquisitionResource assetAcquisitionResourceDecorator;
    private final Mapping<AssetAcquisitionDTO, AssetAcquisitionMTO> assetAcquisitionMTOMapper;
    private final TokenValueSearch<String> stringTokenValueSearch;

    public AssetAcquisitionMuteResourceListener(final IAssetAcquisitionResource assetAcquisitionResourceDecorator, final Mapping<AssetAcquisitionDTO, AssetAcquisitionMTO> assetAcquisitionMTOMapper,
                                                final TokenValueSearch<String> stringTokenValueSearch) {
        this.assetAcquisitionResourceDecorator = assetAcquisitionResourceDecorator;
        this.assetAcquisitionMTOMapper = assetAcquisitionMTOMapper;
        this.stringTokenValueSearch = stringTokenValueSearch;
    }

    @Override
    @StreamListener(AssetAcquisitionResourceStreams.CREATE_RESOURCE_IN)
    public void createAssetAcquisition(@Payload AssetAcquisitionMTO assetAcquisitionMTO) throws URISyntaxException {

        log.debug("Resource DTO received for action : {}", assetAcquisitionMTO);

        MessageTokenDTO messageToken = stringTokenValueSearch.getMessageToken(assetAcquisitionMTO.getMessageToken());

        messageToken.setReceived(true);

        ResponseEntity response = assetAcquisitionResourceDecorator.createAssetAcquisition(assetAcquisitionMTOMapper.toValue1(assetAcquisitionMTO));

        if (response.getStatusCode().is2xxSuccessful()) {
            messageToken.setActioned(true);
        }
    }

    @Override
    @StreamListener(AssetAcquisitionResourceStreams.UPDATE_RESOURCES_IN)
    public void updateAssetAcquisition(@Payload AssetAcquisitionMTO assetAcquisitionMTO) throws URISyntaxException {

        log.debug("Resource DTO received for action : {}", assetAcquisitionMTO);

        MessageTokenDTO messageToken = stringTokenValueSearch.getMessageToken(assetAcquisitionMTO.getMessageToken());

        messageToken.setReceived(true);

        ResponseEntity response = assetAcquisitionResourceDecorator.updateAssetAcquisition(assetAcquisitionMTOMapper.toValue1(assetAcquisitionMTO));

        if (response.getStatusCode().is2xxSuccessful()) {
            messageToken.setActioned(true);
        }
    }

    @Override
    @StreamListener(AssetAcquisitionResourceStreams.DELETE_RESOURCES_IN)
    public void deleteEntity(@Payload DeleteMessageDTO deleteMessageDTO) {

        log.debug("Resource DTO received for action : {}", deleteMessageDTO);

        MessageTokenDTO messageToken = stringTokenValueSearch.getMessageToken(deleteMessageDTO.getMessageToken());

        messageToken.setReceived(true);

        ResponseEntity response = assetAcquisitionResourceDecorator.deleteAssetAcquisition(deleteMessageDTO.getId());

        if (response.getStatusCode().is2xxSuccessful()) {
            messageToken.setActioned(true);
        }
    }
}
