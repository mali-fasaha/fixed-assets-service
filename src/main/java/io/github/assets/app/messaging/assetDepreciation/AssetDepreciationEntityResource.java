package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.DeleteMessageDTO;
import io.github.assets.app.messaging.EntityResource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service("assetDepreciationEntityResource")
public class AssetDepreciationEntityResource implements EntityResource<AssetDepreciationMTO, DeleteMessageDTO> {
}
