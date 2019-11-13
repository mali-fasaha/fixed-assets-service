package io.github.assets.app.resource.decorator;

import io.github.assets.service.dto.AssetDepreciationCriteria;
import io.github.assets.service.dto.AssetDepreciationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

public interface IAssetDepreciationResource {
    /**
     * {@code POST  /asset-depreciations} : Create a new assetDepreciation.
     *
     * @param assetDepreciationDTO the assetDepreciationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetDepreciationDTO, or with status {@code 400 (Bad Request)} if the assetDepreciation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-depreciations")
    ResponseEntity<AssetDepreciationDTO> createAssetDepreciation(@Valid @RequestBody AssetDepreciationDTO assetDepreciationDTO) throws URISyntaxException;

    /**
     * {@code PUT  /asset-depreciations} : Updates an existing assetDepreciation.
     *
     * @param assetDepreciationDTO the assetDepreciationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetDepreciationDTO,
     * or with status {@code 400 (Bad Request)} if the assetDepreciationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetDepreciationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-depreciations")
    ResponseEntity<AssetDepreciationDTO> updateAssetDepreciation(@Valid @RequestBody AssetDepreciationDTO assetDepreciationDTO) throws URISyntaxException;

    /**
     * {@code GET  /asset-depreciations} : get all the assetDepreciations.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetDepreciations in body.
     */
    @GetMapping("/asset-depreciations")
    ResponseEntity<List<AssetDepreciationDTO>> getAllAssetDepreciations(AssetDepreciationCriteria criteria, Pageable pageable);

    /**
    * {@code GET  /asset-depreciations/count} : count all the assetDepreciations.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/asset-depreciations/count")
    ResponseEntity<Long> countAssetDepreciations(AssetDepreciationCriteria criteria);

    /**
     * {@code GET  /asset-depreciations/:id} : get the "id" assetDepreciation.
     *
     * @param id the id of the assetDepreciationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetDepreciationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-depreciations/{id}")
    ResponseEntity<AssetDepreciationDTO> getAssetDepreciation(@PathVariable Long id);

    /**
     * {@code DELETE  /asset-depreciations/:id} : delete the "id" assetDepreciation.
     *
     * @param id the id of the assetDepreciationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-depreciations/{id}")
    ResponseEntity<Void> deleteAssetDepreciation(@PathVariable Long id);

    /**
     * {@code SEARCH  /_search/asset-depreciations?query=:query} : search for the assetDepreciation corresponding
     * to the query.
     *
     * @param query the query of the assetDepreciation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/asset-depreciations")
    ResponseEntity<List<AssetDepreciationDTO>> searchAssetDepreciations(@RequestParam String query, Pageable pageable);
}
