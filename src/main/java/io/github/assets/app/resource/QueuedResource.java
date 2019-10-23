package io.github.assets.app.resource;

import io.github.assets.service.dto.FileUploadDTO;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

public interface QueuedResource<DTO> {

    ResponseEntity<DTO> createEntity(DTO requestDTO) throws URISyntaxException;


    ResponseEntity<FileUploadDTO> updateEntity(DTO requestDTO) throws URISyntaxException;


    ResponseEntity<Void> deleteEntity(Long id);
}
