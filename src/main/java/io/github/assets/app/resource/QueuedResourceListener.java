package io.github.assets.app.resource;

import java.net.URISyntaxException;

public interface QueuedResourceListener<DTO> {

    /**
     * Create entity response
     */
    void createEntity(DTO requestDTO) throws URISyntaxException;


    /**
     * Update entity sent in the request
     */
    void updateEntity(DTO requestDTO) throws URISyntaxException;

    /**
     * Delete the entity whose ID
     */
    void deleteEntity(Long id);
}
