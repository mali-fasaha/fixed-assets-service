package io.github.assets.app.resource;

import io.github.assets.app.messaging.MutationResource;
import io.github.assets.app.resource.decorator.IFileUploadResource;
import io.github.assets.service.dto.FileUploadCriteria;
import io.github.assets.service.dto.FileUploadDTO;
import io.github.assets.service.dto.MessageTokenDTO;
import io.github.assets.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.net.URI;
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

    private final IFileUploadResource fileUploadResource;
    private final MutationResource<FileUploadDTO> fileUploadMutationResource;

    public AppFileUploadResource(final @Qualifier("fileUploadResourceDecorator") IFileUploadResource fileUploadResource, final MutationResource<FileUploadDTO> fileUploadMutationResource) {
        this.fileUploadResource = fileUploadResource;
        this.fileUploadMutationResource = fileUploadMutationResource;
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

        if (fileUploadDTO.getId() != null) {
            throw new BadRequestAlertException("A new fileUpload cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MessageTokenDTO messageToken = fileUploadMutationResource.createEntity(fileUploadDTO);

        // ! Attempting response with message tokens
        return ResponseEntity.created(new URI("/api/message-tokens/" + messageToken.getId()))
                             .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, messageToken.getId().toString()))
                             .body(fileUploadDTO);
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

        if (fileUploadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        fileUploadMutationResource.updateEntity(fileUploadDTO);

        return ResponseEntity.ok(fileUploadDTO);
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

        fileUploadMutationResource.deleteEntity(id);

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/file-uploads?query=:query} : search for the fileUpload corresponding to the query.
     *
     * @param query    the query of the fileUpload search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/file-uploads")
    public ResponseEntity<List<FileUploadDTO>> searchFileUploads(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams,
                                                                 UriComponentsBuilder uriBuilder) {

        return fileUploadResource.searchFileUploads(query, pageable, queryParams, uriBuilder);
    }
}
