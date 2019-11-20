package io.github.assets.app.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.assets.app.messaging.MessageService;
import io.github.assets.app.messaging.TokenizableMessage;
import io.github.assets.app.messaging.fileNotification.FileNotification;
import io.github.assets.app.resource.decorator.IFileUploadResource;
import io.github.assets.app.util.TokenGenerator;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.FileUploadCriteria;
import io.github.assets.service.dto.FileUploadDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing {@link io.github.assets.domain.FileUpload}.
 * <p>
 * It is intended to enable asynchronous processing for PUT, POST and DELETE request
 */
@Slf4j
@RestController
@RequestMapping("/api/app")
public class AppFileUploadResource implements IFileUploadResource {

    private static final String ENTITY_NAME = "fixedAssetServiceFileUpload";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TokenGenerator tokenGenerator;
    private final IFileUploadResource fileUploadResource;
    private final MessageTokenService messageTokenService;
    private final MessageService<TokenizableMessage<String>> fileUploadNotificationMessageService;

    public AppFileUploadResource(final IFileUploadResource fileUploadResourceDecorator, final MessageTokenService messageTokenService,
                                 final MessageService<TokenizableMessage<String>> fileUploadNotificationMessageService, final TokenGenerator tokenGenerator) {
        this.fileUploadResource = fileUploadResourceDecorator;
        this.messageTokenService = messageTokenService;
        this.fileUploadNotificationMessageService = fileUploadNotificationMessageService;
        this.tokenGenerator = tokenGenerator;
    }

    /**
     * {@code POST  /file-uploads} : Create a new fileUpload.
     *
     * @param fileUploadDTO the fileUploadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileUploadDTO, or with status {@code 400 (Bad Request)} if the fileUpload has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    @PostMapping("/file-uploads")
    public ResponseEntity<FileUploadDTO> createFileUpload(@Valid @RequestBody FileUploadDTO fileUploadDTO) throws URISyntaxException {

        log.info("File-Upload received : {}", fileUploadDTO);

        // TODO Generate message tokens
        String token = null;
        try {
            token = tokenGenerator.md5Digest(fileUploadDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        ResponseEntity<FileUploadDTO> fileUploadResponseEntity = fileUploadResource.createFileUpload(fileUploadDTO);

        // TODO Notifications
        MessageTokenDTO messageToken =
            messageTokenService.save(
                fileUploadNotificationMessageService.sendMessage(FileNotification.builder()
                                                                                 .fileId(String.valueOf(fileUploadResponseEntity.getBody().getId()))
                                                                                 .timestamp(System.currentTimeMillis())
                                                                                 .filename(fileUploadResponseEntity.getBody().getFileName())
                                                                                 .messageToken(token)
                                                                                 .description(fileUploadDTO.getDescription())
                                                                                 .build()));
        log.info("File successfully enqueued with token # : {}", messageToken.getTokenValue());

        // TODO add token field to fileUpload
        return fileUploadResponseEntity;
    }

    /**
     * {@code PUT  /file-uploads} : Updates an existing fileUpload.
     *
     * @param fileUploadDTO the fileUploadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileUploadDTO, or with status {@code 400 (Bad Request)} if the fileUploadDTO is not valid, or with
     * status {@code 500 (Internal Server Error)} if the fileUploadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    @PutMapping("/file-uploads")
    public ResponseEntity<FileUploadDTO> updateFileUpload(@Valid @RequestBody FileUploadDTO fileUploadDTO) throws URISyntaxException {

        return fileUploadResource.updateFileUpload(fileUploadDTO);
    }

    /**
     * {@code GET  /file-uploads} : get all the fileUploads.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileUploads in body.
     */
    @GetMapping("/file-uploads")
    public ResponseEntity<List<FileUploadDTO>> getAllFileUploads(FileUploadCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams,
                                                                 UriComponentsBuilder uriBuilder) {

        return this.fileUploadResource.getAllFileUploads(criteria, pageable, queryParams, uriBuilder);
    }

    /**
     * {@code GET  /file-uploads/count} : count all the fileUploads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/file-uploads/count")
    public ResponseEntity<Long> countFileUploads(FileUploadCriteria criteria) {

        return this.fileUploadResource.countFileUploads(criteria);
    }

    /**
     * {@code GET  /file-uploads/:id} : get the "id" fileUpload.
     *
     * @param id the id of the fileUploadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileUploadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-uploads/{id}")
    public ResponseEntity<FileUploadDTO> getFileUpload(@PathVariable Long id) {

        return fileUploadResource.getFileUpload(id);
    }

    /**
     * {@code DELETE  /file-uploads/:id} : delete the "id" fileUpload.
     *
     * @param id the id of the fileUploadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Override
    @DeleteMapping("/file-uploads/{id}")
    public ResponseEntity<Void> deleteFileUpload(@PathVariable Long id) {

        return fileUploadResource.deleteFileUpload(id);
    }
}
