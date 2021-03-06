package io.github.assets.app.resource.decorator;

import io.github.assets.service.dto.FileUploadCriteria;
import io.github.assets.service.dto.FileUploadDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;
import java.util.List;

public interface IFileUploadResource {
    /**
     * {@code POST  /file-uploads} : Create a new fileUpload.
     *
     * @param fileUploadDTO the fileUploadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileUploadDTO, or with status {@code 400 (Bad Request)} if the fileUpload has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    ResponseEntity<FileUploadDTO> createFileUpload(FileUploadDTO fileUploadDTO) throws URISyntaxException;

    /**
     * {@code PUT  /file-uploads} : Updates an existing fileUpload.
     *
     * @param fileUploadDTO the fileUploadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileUploadDTO,
     * or with status {@code 400 (Bad Request)} if the fileUploadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileUploadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    ResponseEntity<FileUploadDTO> updateFileUpload(FileUploadDTO fileUploadDTO) throws URISyntaxException;

    /**
     * {@code GET  /file-uploads} : get all the fileUploads.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileUploads in body.
     */
    ResponseEntity<List<FileUploadDTO>> getAllFileUploads(FileUploadCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder);

    /**
    * {@code GET  /file-uploads/count} : count all the fileUploads.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    ResponseEntity<Long> countFileUploads(FileUploadCriteria criteria);

    /**
     * {@code GET  /file-uploads/:id} : get the "id" fileUpload.
     *
     * @param id the id of the fileUploadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileUploadDTO, or with status {@code 404 (Not Found)}.
     */
    ResponseEntity<FileUploadDTO> getFileUpload(Long id);

    /**
     * {@code DELETE  /file-uploads/:id} : delete the "id" fileUpload.
     *
     * @param id the id of the fileUploadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    ResponseEntity<Void> deleteFileUpload(Long id);

    /**
     * {@code SEARCH  /_search/file-uploads?query=:query} : search for the fileUpload corresponding
     * to the query.
     *
     * @param query the query of the fileUpload search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    ResponseEntity<List<FileUploadDTO>> searchFileUploads(String query, Pageable pageable, MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder);
}
