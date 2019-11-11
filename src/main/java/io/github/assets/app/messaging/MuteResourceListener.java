package io.github.assets.app.messaging;

import java.net.URISyntaxException;

public interface MuteResourceListener<MTO> {

    void createEntityAcquisition(MTO dto) throws URISyntaxException;

    void updateEntityAcquisition(MTO dto) throws URISyntaxException;

    void deleteEntity(DeleteMessageDTO deleteMessageDTO);
}
