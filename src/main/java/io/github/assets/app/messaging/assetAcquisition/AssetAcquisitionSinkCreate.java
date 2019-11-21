package io.github.assets.app.messaging.assetAcquisition;

import com.google.common.collect.ImmutableList;
import io.github.assets.app.Mapping;
import io.github.assets.app.messaging.GsonUtils;
import io.github.assets.app.messaging.platform.MuteListener;
import io.github.assets.app.messaging.jsonStrings.JsonStringStreams;
import io.github.assets.app.messaging.jsonStrings.StringMessageDTO;
import io.github.assets.app.model.AssetAcquisitionEVM;
import io.github.assets.service.AssetAcquisitionService;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This is some sort of a sink capable of receiving a JSON string and persisting it as AssetAcquisition items using the service
 */
@Slf4j
@Transactional
@Service("assetAcquisitionSinkCreate")
public class AssetAcquisitionSinkCreate implements MuteListener<StringMessageDTO> {

    // TODO test this class

    private final AssetAcquisitionService assetAcquisitionService;
    // TODO Add this to container
    private final Mapping<AssetAcquisitionDTO, AssetAcquisitionEVM> assetAcquisitionDTOEVMMapping;

    public AssetAcquisitionSinkCreate(final AssetAcquisitionService assetAcquisitionService, final Mapping<AssetAcquisitionDTO, AssetAcquisitionEVM> assetAcquisitionDTOEVMMapping) {
        this.assetAcquisitionService = assetAcquisitionService;
        this.assetAcquisitionDTOEVMMapping = assetAcquisitionDTOEVMMapping;
    }

    @StreamListener(JsonStringStreams.JSON_ACQUISITIONS_CREATE_INBOUND)
    public void handleMessage(@Payload StringMessageDTO message) {

        log.info("JSON string list items received for persistence : {}", message);

        List<AssetAcquisitionEVM> acquisitionData = GsonUtils.stringToList(message.getJsonString(), AssetAcquisitionEVM[].class);

        List<AssetAcquisitionDTO> persistedAcquisition =
            acquisitionData.stream().map(assetAcquisitionDTOEVMMapping::toValue1).map(assetAcquisitionService::save).collect(ImmutableList.toImmutableList());

        log.info("{} Items persisted to the sink", persistedAcquisition.size());
    }
}
