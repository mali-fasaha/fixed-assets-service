package io.github.assets.app.resource;

import io.github.assets.service.dto.FileUploadDTO;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

public interface QueuedResource<DTO> {

    /**
     * Create entity response
     *
     * @param requestDTO
     * @return
     * @throws URISyntaxException
     */
    ResponseEntity<DTO> createEntity(DTO requestDTO) throws URISyntaxException;


    /**
     * Update entity sent in the request
     *
     * @param requestDTO
     * @return
     * @throws URISyntaxException
     */
    ResponseEntity<FileUploadDTO> updateEntity(DTO requestDTO) throws URISyntaxException;

    /**
     * Delete the entity whose ID
     *
     * @param id
     * @return
     */
    ResponseEntity<Void> deleteEntity(Long id);
}
