package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.EntityResource;
import io.github.assets.app.messaging.Mapping;
import io.github.assets.app.resource.decorator.IAssetDepreciationResource;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.AssetDepreciationDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.net.URISyntaxException;

@Transactional
@Service("assetDepreciationEntityResource")
public class AssetDepreciationEntityResource implements EntityResource<AssetDepreciationMTO, DeleteMessageDTO> {

    private final IAssetDepreciationResource assetDepreciationResourceDecorator;
    private final Mapping<AssetDepreciationDTO, AssetDepreciationMTO> assetDepreciationMTOMapping;
    private final MessageTokenService messageTokenService;

    public AssetDepreciationEntityResource(final IAssetDepreciationResource assetDepreciationResourceDecorator, final Mapping<AssetDepreciationDTO, AssetDepreciationMTO> assetDepreciationMTOMapping,
                                           final MessageTokenService messageTokenService) {
        this.assetDepreciationResourceDecorator = assetDepreciationResourceDecorator;
        this.assetDepreciationMTOMapping = assetDepreciationMTOMapping;
        this.messageTokenService = messageTokenService;
    }

    @Override
    public void createEntity(final AssetDepreciationMTO entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException {

        ResponseEntity response = assetDepreciationResourceDecorator.createAssetDepreciation(assetDepreciationMTOMapping.toValue1(entityMTO));

        if (response.getStatusCode().is2xxSuccessful()) {
            messageToken.setActioned(true);
        }

        messageTokenService.save(messageToken);
    }

    @Override
    public void updateEntity(final AssetDepreciationMTO entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException {

        ResponseEntity response = assetDepreciationResourceDecorator.updateAssetDepreciation(assetDepreciationMTOMapping.toValue1(entityMTO));

        if (response.getStatusCode().is2xxSuccessful()) {
            messageToken.setActioned(true);
        }

        messageTokenService.save(messageToken);
    }

    @Override
    public void deleteEntity(final DeleteMessageDTO deleteMessageDTO, final MessageTokenDTO messageToken) {

        ResponseEntity response = assetDepreciationResourceDecorator.deleteAssetDepreciation(deleteMessageDTO.getId());

        if (response.getStatusCode().is2xxSuccessful()) {
            messageToken.setActioned(true);
        }

        messageTokenService.save(messageToken);
    }
}
