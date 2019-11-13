package io.github.assets.app.messaging;

import io.github.assets.service.dto.MessageTokenDTO;

import java.net.URISyntaxException;

public interface EntityResource<M, D> {

    void createEntity(final M entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException;

    void updateEntity(final M entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException;

    void deleteEntity(final D deleteMessageDTO, final MessageTokenDTO messageToken);
}
