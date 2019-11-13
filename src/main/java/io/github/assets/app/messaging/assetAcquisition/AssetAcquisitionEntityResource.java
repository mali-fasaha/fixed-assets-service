package io.github.assets.app.messaging.assetAcquisition;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.EntityResource;
import io.github.assets.app.messaging.Mapping;
import io.github.assets.app.resource.decorator.IAssetAcquisitionResource;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URISyntaxException;

@Transactional
@Service("assetAcquisitionEntityResource")
public class AssetAcquisitionEntityResource implements EntityResource<AssetAcquisitionMTO, DeleteMessageDTO> {

    private final IAssetAcquisitionResource assetAcquisitionResourceDecorator;
    private final Mapping<AssetAcquisitionDTO, AssetAcquisitionMTO> assetAcquisitionMTOMapper;
    private final MessageTokenService messageTokenService;

    public AssetAcquisitionEntityResource(final IAssetAcquisitionResource assetAcquisitionResourceDecorator, final Mapping<AssetAcquisitionDTO, AssetAcquisitionMTO> assetAcquisitionMTOMapper,
                                          final MessageTokenService messageTokenService) {
        this.assetAcquisitionResourceDecorator = assetAcquisitionResourceDecorator;
        this.assetAcquisitionMTOMapper = assetAcquisitionMTOMapper;
        this.messageTokenService = messageTokenService;
    }

    public void createEntity(final AssetAcquisitionMTO assetAcquisitionMTO, final MessageTokenDTO messageToken) throws URISyntaxException {
        ResponseEntity response = assetAcquisitionResourceDecorator.createAssetAcquisition(assetAcquisitionMTOMapper.toValue1(assetAcquisitionMTO));

        if (response.getStatusCode().is2xxSuccessful()) {
            messageToken.setActioned(true);
        }

        messageTokenService.save(messageToken);
    }

    public void updateEntity(final AssetAcquisitionMTO assetAcquisitionMTO, final MessageTokenDTO messageToken) throws URISyntaxException {
        ResponseEntity response = assetAcquisitionResourceDecorator.updateAssetAcquisition(assetAcquisitionMTOMapper.toValue1(assetAcquisitionMTO));

        if (response.getStatusCode().is2xxSuccessful()) {
            messageToken.setActioned(true);
        }

        messageTokenService.save(messageToken);
    }

    public void deleteEntity(final DeleteMessageDTO deleteMessageDTO, final MessageTokenDTO messageToken) {
        ResponseEntity response = assetAcquisitionResourceDecorator.deleteAssetAcquisition(deleteMessageDTO.getId());

        if (response.getStatusCode().is2xxSuccessful()) {
            messageToken.setActioned(true);
        }

        messageTokenService.save(messageToken);
    }
}
