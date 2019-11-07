package io.github.assets.app.messaging;

import io.github.assets.domain.MessageToken;
import io.github.assets.service.dto.MessageTokenDTO;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

/**
 * This resource carries out mutative activities on the data over whose entity it controls
 */
public interface MutationResource<DTO> {

    MessageTokenDTO createAssetAcquisition(DTO dto) throws URISyntaxException;

    MessageTokenDTO updateAssetAcquisition(DTO dto) throws URISyntaxException;

    MessageTokenDTO deleteEntity(Long id);

}
