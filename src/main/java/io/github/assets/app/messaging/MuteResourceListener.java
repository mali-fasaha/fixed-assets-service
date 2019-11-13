package io.github.assets.app.messaging;

import java.net.URISyntaxException;

public interface MuteResourceListener<MTO> {

    void createEntity(MTO dto) throws URISyntaxException;

    void updateEntity(MTO dto) throws URISyntaxException;

    void deleteEntity(DeleteMessageDTO deleteMessageDTO);
}
