package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.EntityResource;
import io.github.assets.service.dto.MessageTokenDTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URISyntaxException;

@Transactional
@Service("assetDepreciationEntityResource")
public class AssetDepreciationEntityResource implements EntityResource<AssetDepreciationMTO, DeleteMessageDTO> {

    @Override
    public void createEntity(final AssetDepreciationMTO entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException {

    }

    @Override
    public void updateEntity(final AssetDepreciationMTO entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException {

    }

    @Override
    public void deleteEntity(final DeleteMessageDTO deleteMessageDTO, final MessageTokenDTO messageToken) {

    }
}
