package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.Mapping;
import io.github.assets.app.messaging.MuteResourceListener;
import io.github.assets.app.resource.decorator.IAssetAcquisitionResource;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URISyntaxException;

@Slf4j
@Service("assetAcquisitionMuteResourceListener")
@Transactional
public class AssetAcquisitionMuteResourceListener implements MuteResourceListener<AssetAcquisitionMTO> {

    private final IAssetAcquisitionResource assetAcquisitionResourceDecorator;
    private final Mapping<AssetAcquisitionDTO, AssetAcquisitionMTO> assetAcquisitionMTOMapper;

    public AssetAcquisitionMuteResourceListener(final IAssetAcquisitionResource assetAcquisitionResourceDecorator, final Mapping<AssetAcquisitionDTO, AssetAcquisitionMTO> assetAcquisitionMTOMapper) {
        this.assetAcquisitionResourceDecorator = assetAcquisitionResourceDecorator;
        this.assetAcquisitionMTOMapper = assetAcquisitionMTOMapper;
    }

    @Override
    @StreamListener(AssetAcquisitionResourceStreams.CREATE_RESOURCE_IN)
    public void createAssetAcquisition(@Payload AssetAcquisitionMTO assetAcquisitionMTO) throws URISyntaxException {

        log.debug("Resource DTO received for action : {}", assetAcquisitionMTO);

        assetAcquisitionMTO.getMessageToken();

        assetAcquisitionResourceDecorator.createAssetAcquisition(assetAcquisitionMTOMapper.toValue1(assetAcquisitionMTO));
    }

    @Override
    @StreamListener(AssetAcquisitionResourceStreams.UPDATE_RESOURCES_IN)
    public void updateAssetAcquisition(@Payload AssetAcquisitionMTO assetAcquisitionMTO) throws URISyntaxException {

        log.debug("Resource DTO received for action : {}", assetAcquisitionMTO);

        assetAcquisitionResourceDecorator.updateAssetAcquisition(assetAcquisitionMTOMapper.toValue1(assetAcquisitionMTO));
    }

    @Override
    @StreamListener(AssetAcquisitionResourceStreams.DELETE_RESOURCES_IN)
    public void deleteEntity(@Payload DeleteMessageDTO deleteMessageDTO) {

        log.debug("Resource DTO received for action : {}", deleteMessageDTO);

        assetAcquisitionResourceDecorator.deleteAssetAcquisition(deleteMessageDTO.getId());
    }
}
