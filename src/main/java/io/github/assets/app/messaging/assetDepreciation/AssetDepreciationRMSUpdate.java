package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.MessageService;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Transactional
@Service("assetDepreciationRMSUpdate")
public class AssetDepreciationRMSUpdate implements MessageService<AssetAcquisitionDTO> {
}
