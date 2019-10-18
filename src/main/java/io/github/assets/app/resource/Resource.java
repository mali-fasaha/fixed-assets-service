package io.github.assets.app.resource;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.List;

public interface Resource<EntityDTO, Criteria> {

    /**
     * {@code POST  /entities} : Create a new fileUpload.
     *
     * @param entityDTO the entityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entityDTO, or with status {@code 400 (Bad Request)} if the entity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    ResponseEntity<EntityDTO> createEntity(EntityDTO entityDTO) throws URISyntaxException;

    /**
     * {@code PUT  /entities} : Updates an existing entity
     *
     * @param entityDTO the entityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entityDTO,
     * or with status {@code 400 (Bad Request)} if the entityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    ResponseEntity<EntityDTO> updateEntity(EntityDTO entityDTO) throws URISyntaxException;

    /**
     * {@code GET  /entities} : get all the entities.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entities in body.
     */
    ResponseEntity<List<EntityDTO>> getAllEntities(Criteria criteria, Pageable pageable);

    /**
    * {@code GET  /entities/count} : count all the entities.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    ResponseEntity<Long> countEntities(Criteria criteria);

    /**
     * {@code GET  /entities/:id} : get the "id" fileUpload.
     *
     * @param id the id of the entityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entityDTO, or with status {@code 404 (Not Found)}.
     */
    ResponseEntity<EntityDTO> getEntity(Long id);

    /**
     * {@code DELETE  /entities/:id} : delete the "id" fileUpload.
     *
     * @param id the id of the entityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    ResponseEntity<Void> deleteEntity(Long id);

    /**
     * {@code SEARCH  /_search/entities?query=:query} : search for the fileUpload corresponding
     * to the query.
     *
     * @param query the query of the fileUpload search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    ResponseEntity<List<EntityDTO>> searchEntities(String query, Pageable pageable);
}
