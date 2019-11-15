package io.github.assets.app.messaging.assetAcquisition;

import com.google.common.collect.ImmutableList;
import io.github.assets.app.messaging.GsonUtils;
import io.github.assets.app.Mapping;
import io.github.assets.app.messaging.MuteListener;
import io.github.assets.app.model.AssetAcquisitionEVM;
import io.github.assets.service.AssetAcquisitionService;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This is some sort of a sink capable of receiving a JSON string and persisting it as AssetAcquisition
 * items using the service
 */
@Slf4j
@Transactional
@Service("assetAcquisitionSinkCreate")
public class AssetAcquisitionSinkCreate implements MuteListener<Message<String>> {

    private final AssetAcquisitionService assetAcquisitionService;
    // TODO Add this to container
    private final Mapping<AssetAcquisitionDTO, AssetAcquisitionEVM> assetAcquisitionDTOAssetAcquisitionEVMMapping;

    public AssetAcquisitionSinkCreate(final AssetAcquisitionService assetAcquisitionService, final Mapping<AssetAcquisitionDTO, AssetAcquisitionEVM> assetAcquisitionDTOAssetAcquisitionEVMMapping) {
        this.assetAcquisitionService = assetAcquisitionService;
        this.assetAcquisitionDTOAssetAcquisitionEVMMapping = assetAcquisitionDTOAssetAcquisitionEVMMapping;
    }

    @StreamListener(AssetAcquisitionResourceStreams.FILED_CREATE_RESOURCE_IN)
    public void handleMessage(@Payload Message<String> message) {

        List<AssetAcquisitionEVM> acquisitionData = GsonUtils.stringToList(message.getPayload(), AssetAcquisitionEVM[].class);

        List<AssetAcquisitionDTO> persistedAcquisition =
            acquisitionData.stream().map(assetAcquisitionDTOAssetAcquisitionEVMMapping::toValue1).map(assetAcquisitionService::save).collect(ImmutableList.toImmutableList());

        log.info("{} Items persisted to the sink", persistedAcquisition.size());
    }
}
