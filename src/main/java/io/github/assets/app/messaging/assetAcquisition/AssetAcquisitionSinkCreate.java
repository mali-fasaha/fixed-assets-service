package io.github.assets.app.messaging.assetAcquisition;

import com.google.common.collect.ImmutableList;
import io.github.assets.app.messaging.GsonUtils;
import io.github.assets.app.messaging.Mapping;
import io.github.assets.app.messaging.MuteListener;
import io.github.assets.app.model.AssetAcquisitionEVM;
import io.github.assets.service.AssetAcquisitionService;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("assetAcquisitionSinkCreate")
public class AssetAcquisitionSinkCreate implements MuteListener<Message<String>> {

    // TODO test this class

    private final AssetAcquisitionService assetAcquisitionService;
    // TODO Add this to container
    private final Mapping<AssetAcquisitionDTO, AssetAcquisitionEVM> assetAcquisitionDTOAssetAcquisitionEVMMapping;

    public AssetAcquisitionSinkCreate(final AssetAcquisitionService assetAcquisitionService, final Mapping<AssetAcquisitionDTO, AssetAcquisitionEVM> assetAcquisitionDTOAssetAcquisitionEVMMapping) {
        this.assetAcquisitionService = assetAcquisitionService;
        this.assetAcquisitionDTOAssetAcquisitionEVMMapping = assetAcquisitionDTOAssetAcquisitionEVMMapping;
    }

    // TODO

    @StreamListener(AssetAcquisitionResourceStreams.FILED_CREATE_RESOURCE_IN)
    public void handleMessage(@Payload Message<String> jsonString) {

        List<AssetAcquisitionEVM> acquisitionData = GsonUtils.stringToList(jsonString.getPayload(), AssetAcquisitionEVM[].class);

        List<AssetAcquisitionDTO> persistedAcquisition =
            acquisitionData.stream().map(assetAcquisitionDTOAssetAcquisitionEVMMapping::toValue1).map(assetAcquisitionService::save).collect(ImmutableList.toImmutableList());
    }
}
