package io.github.assets.app.messaging.platform;

import io.github.assets.service.dto.MessageTokenDTO;

import java.net.URISyntaxException;

/**
 * This resource carries out mutative activities on the data over whose entity it controls
 */
public interface MutationResource<DTO> {

    MessageTokenDTO createEntity(DTO dto) throws URISyntaxException;

    MessageTokenDTO updateEntity(DTO dto) throws URISyntaxException;

    MessageTokenDTO deleteEntity(Long id);

}
