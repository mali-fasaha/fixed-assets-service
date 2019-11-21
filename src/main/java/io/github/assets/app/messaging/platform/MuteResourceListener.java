package io.github.assets.app.messaging.platform;

import io.github.assets.app.messaging.DeleteMessageDTO;

import java.net.URISyntaxException;

public interface MuteResourceListener<MTO> {

    void createEntity(MTO dto) throws URISyntaxException;

    void updateEntity(MTO dto) throws URISyntaxException;

    void deleteEntity(DeleteMessageDTO deleteMessageDTO);
}
