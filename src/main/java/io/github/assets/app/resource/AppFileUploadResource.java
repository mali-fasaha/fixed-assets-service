package io.github.assets.app.resource;

import io.github.assets.app.resource.decorator.FileUploadResource;
import io.github.assets.app.resource.decorator.IFileUploadResource;
import io.github.assets.service.FileUploadQueryService;
import io.github.assets.service.FileUploadService;
import io.github.assets.service.dto.FileUploadCriteria;
import io.github.assets.service.dto.FileUploadDTO;
import io.github.assets.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/app")
public class AppFileUploadResource implements IFileUploadResource {

//    private final IFileUploadResource fileUploadResource;
//
//    public AppFileUploadResource(final IFileUploadResource fileUploadResource) {
//        this.fileUploadResource = fileUploadResource;
//    }

    private static final String ENTITY_NAME = "fixedAssetServiceFileUpload";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileUploadService fileUploadService;

    private final FileUploadQueryService fileUploadQueryService;

    public AppFileUploadResource(FileUploadService fileUploadService, FileUploadQueryService fileUploadQueryService) {
        this.fileUploadService = fileUploadService;
        this.fileUploadQueryService = fileUploadQueryService;
    }

    /**
     * {@code POST  /file-uploads} : Create a new fileUpload.
     *
     * @param fileUploadDTO the fileUploadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileUploadDTO, or with status {@code 400 (Bad Request)} if the fileUpload has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-uploads")
    public ResponseEntity<FileUploadDTO> createFileUpload(@Valid @RequestBody FileUploadDTO fileUploadDTO) throws URISyntaxException {
        log.debug("REST request to save FileUpload : {}", fileUploadDTO);
        if (fileUploadDTO.getId() != null) {
            throw new BadRequestAlertException("A new fileUpload cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileUploadDTO result = fileUploadService.save(fileUploadDTO);
        return ResponseEntity.created(new URI("/api/file-uploads/" + result.getId()))
                             .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                             .body(result);
    }

    /**
     * {@code PUT  /file-uploads} : Updates an existing fileUpload.
     *
     * @param fileUploadDTO the fileUploadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileUploadDTO,
     * or with status {@code 400 (Bad Request)} if the fileUploadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileUploadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/file-uploads")
    public ResponseEntity<FileUploadDTO> updateFileUpload(@Valid @RequestBody FileUploadDTO fileUploadDTO) throws URISyntaxException {
        log.debug("REST request to update FileUpload : {}", fileUploadDTO);
        if (fileUploadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FileUploadDTO result = fileUploadService.save(fileUploadDTO);
        return ResponseEntity.ok()
                             .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fileUploadDTO.getId().toString()))
                             .body(result);
    }

    /**
     * {@code GET  /file-uploads} : get all the fileUploads.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileUploads in body.
     */
    @GetMapping("/file-uploads")
    public ResponseEntity<List<FileUploadDTO>> getAllFileUploads(FileUploadCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FileUploads by criteria: {}", criteria);
        Page<FileUploadDTO> page = fileUploadQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /file-uploads/count} : count all the fileUploads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/file-uploads/count")
    public ResponseEntity<Long> countFileUploads(FileUploadCriteria criteria) {
        log.debug("REST request to count FileUploads by criteria: {}", criteria);
        return ResponseEntity.ok().body(fileUploadQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /file-uploads/:id} : get the "id" fileUpload.
     *
     * @param id the id of the fileUploadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileUploadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-uploads/{id}")
    public ResponseEntity<FileUploadDTO> getFileUpload(@PathVariable Long id) {
        log.debug("REST request to get FileUpload : {}", id);
        Optional<FileUploadDTO> fileUploadDTO = fileUploadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileUploadDTO);
    }

    /**
     * {@code DELETE  /file-uploads/:id} : delete the "id" fileUpload.
     *
     * @param id the id of the fileUploadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/file-uploads/{id}")
    public ResponseEntity<Void> deleteFileUpload(@PathVariable Long id) {
        log.debug("REST request to delete FileUpload : {}", id);
        fileUploadService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/file-uploads?query=:query} : search for the fileUpload corresponding
     * to the query.
     *
     * @param query the query of the fileUpload search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/file-uploads")
    public ResponseEntity<List<FileUploadDTO>> searchFileUploads(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FileUploads for query {}", query);
        Page<FileUploadDTO> page = fileUploadService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
