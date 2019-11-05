package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.Mapping;
import io.github.assets.app.messaging.MuteResourceListener;
import io.github.assets.app.resource.decorator.IAssetAcquisitionResource;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URISyntaxException;

@Service
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
        assetAcquisitionResourceDecorator.createAssetAcquisition(assetAcquisitionMTOMapper.toValue1(assetAcquisitionMTO));
    }

    @Override
    @StreamListener(AssetAcquisitionResourceStreams.UPDATE_RESOURCES_IN)
    public void updateAssetAcquisition(@Payload AssetAcquisitionMTO assetAcquisitionMTO) throws URISyntaxException {
        assetAcquisitionResourceDecorator.updateAssetAcquisition(assetAcquisitionMTOMapper.toValue1(assetAcquisitionMTO));
    }

    @Override
    @StreamListener(AssetAcquisitionResourceStreams.DELETE_RESOURCES_IN)
    public void deleteEntity(@Payload Message<Long> id) {
        assetAcquisitionResourceDecorator.deleteAssetAcquisition(id.getPayload());
    }
}
