package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.MutationResource;
import io.github.assets.service.dto.AssetDepreciationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service("assetDepreciationMutationResourcee")
public class AssetDepreciationMutationResource implements MutationResource<AssetDepreciationDTO> {
}
