package io.github.assets.app.messaging;

import io.github.assets.domain.MessageToken;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

/**
 * This resource carries out mutative activities on the data over whose entity it controls
 */
public interface MutationResource<DTO> {

    MessageToken createAssetAcquisition(DTO dto) throws URISyntaxException;

    MessageToken updateAssetAcquisition(DTO dto) throws URISyntaxException;

    MessageToken deleteEntity(Long id);

}
