package io.github.assets.app.messaging;

import java.net.URISyntaxException;

public interface MuteResourceListener<MTO> {

    void createAssetAcquisition(MTO dto) throws URISyntaxException;

    void updateAssetAcquisition(MTO dto) throws URISyntaxException;

    void deleteEntity(DeleteMessageDTO deleteMessageDTO);
}
