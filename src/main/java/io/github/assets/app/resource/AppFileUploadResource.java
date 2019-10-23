package io.github.assets.app.resource;

import io.github.assets.app.resource.decorator.DecoratorFileUploadResource;
import io.github.assets.app.resource.decorator.IFileUploadResource;
import io.github.assets.service.FileUploadQueryService;
import io.github.assets.service.FileUploadService;
import io.github.assets.service.dto.FileUploadCriteria;
import io.github.assets.service.dto.FileUploadDTO;
import io.github.assets.app.resource.decorator.FileUploadResource;
import org.springframework.data.domain.Pageable;
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

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/app")
public class AppFileUploadResource extends FileUploadResource implements Resource<FileUploadDTO, FileUploadCriteria> {

    public AppFileUploadResource(final FileUploadService fileUploadService, final FileUploadQueryService fileUploadQueryService) {
        super(fileUploadService, fileUploadQueryService);
    }

    /**
     * {@code POST  /entities} : Create a new fileUpload.
     *
     * @param fileUploadDTO the entityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entityDTO, or with status {@code 400 (Bad Request)} if the entity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    @PostMapping("/deco-file-uploads")
    public ResponseEntity<FileUploadDTO> createEntity(@Valid @RequestBody final FileUploadDTO fileUploadDTO) throws URISyntaxException {

        return super.createFileUpload(fileUploadDTO);
    }

    /**
     * {@code PUT  /entities} : Updates an existing entity
     *
     * @param fileUploadDTO the entityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entityDTO, or with status {@code 400 (Bad Request)} if the entityDTO is not valid, or with status
     * {@code 500 (Internal Server Error)} if the entityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    @PutMapping("/deco-file-uploads")
    public ResponseEntity<FileUploadDTO> updateEntity(@Valid @RequestBody final FileUploadDTO fileUploadDTO) throws URISyntaxException {
        return super.updateFileUpload(fileUploadDTO);
    }

    /**
     * {@code GET  /entities} : get all the entities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entities in body.
     */
    @Override
    @GetMapping("/deco-file-uploads")
    public ResponseEntity<List<FileUploadDTO>> getAllEntities(final FileUploadCriteria criteria, final Pageable pageable) {
        return super.getAllFileUploads(criteria, pageable);
    }

    /**
     * {@code GET  /entities/count} : count all the entities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @Override
    @GetMapping("/deco-file-uploads/count")
    public ResponseEntity<Long> countEntities(final FileUploadCriteria criteria) {
        return super.countFileUploads(criteria);
    }

    /**
     * {@code GET  /entities/:id} : get the "id" fileUpload.
     *
     * @param id the id of the entityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entityDTO, or with status {@code 404 (Not Found)}.
     */
    @Override
    @GetMapping("/deco-file-uploads/{id}")
    public ResponseEntity<FileUploadDTO> getEntity(@PathVariable final Long id) {
        return super.getFileUpload(id);
    }

    /**
     * {@code DELETE  /entities/:id} : delete the "id" fileUpload.
     *
     * @param id the id of the entityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Override
    @DeleteMapping("/deco-file-uploads/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable final Long id) {
        return super.deleteFileUpload(id);
    }

    /**
     * {@code SEARCH  /_search/entities?query=:query} : search for the fileUpload corresponding to the query.
     *
     * @param query    the query of the fileUpload search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @Override
    @GetMapping("/_search/deco-file-uploads")
    public ResponseEntity<List<FileUploadDTO>> searchEntities(@RequestParam final String query, final Pageable pageable) {
        return super.searchFileUploads(query, pageable);
    }
}
