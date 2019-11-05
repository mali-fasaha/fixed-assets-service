package io.github.assets.app.messaging;

import org.springframework.messaging.Message;

import java.net.URISyntaxException;

public interface MuteResourceListener<DTO> {

    void createAssetAcquisition(DTO dto) throws URISyntaxException;

    void updateAssetAcquisition(DTO dto) throws URISyntaxException;

    void deleteEntity(Message<Long> id);
}
