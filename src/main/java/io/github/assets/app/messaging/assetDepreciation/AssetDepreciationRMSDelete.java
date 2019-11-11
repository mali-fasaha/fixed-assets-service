package io.github.assets.app.messaging.assetDepreciation;

import io.github.assets.app.messaging.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Transactional
@Service("assetDepreciationRMSDelete")
public class AssetDepreciationRMSDelete implements MessageService<Long> {
}
