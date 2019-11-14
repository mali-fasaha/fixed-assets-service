package io.github.assets.app.messaging;

import io.github.assets.service.dto.MessageTokenDTO;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

public interface ResponsiveEntityResource<M, D, R> extends EntityResource<M, D> {


    ResponseEntity<R> createEntityAndRespond(final M entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException;

    ResponseEntity<R> updateEntityAndRespond(final M entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException;

    ResponseEntity<Void> deleteEntityAndRespond(final D deleteMessageDTO, final MessageTokenDTO messageToken);

    @Override
    default void createEntity(final M entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException {
        this.createEntityAndRespond(entityMTO, messageToken);
    }

    @Override
    default void updateEntity(final M entityMTO, final MessageTokenDTO messageToken) throws URISyntaxException {
        this.updateEntityAndRespond(entityMTO, messageToken);
    }

    @Override
    default void deleteEntity(final D deleteMessageDTO, final MessageTokenDTO messageToken) {
        this.deleteEntityAndRespond(deleteMessageDTO, messageToken);
    }
}
