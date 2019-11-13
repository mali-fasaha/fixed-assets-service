package io.github.assets.app.resource.decorator;

import io.github.assets.service.dto.AssetAcquisitionCriteria;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

public interface IAssetAcquisitionResource {
    /**
     * {@code POST  /asset-acquisitions} : Create a new assetAcquisition.
     *
     * @param assetAcquisitionDTO the assetAcquisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetAcquisitionDTO, or with status {@code 400 (Bad Request)} if the assetAcquisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    ResponseEntity<AssetAcquisitionDTO> createAssetAcquisition(AssetAcquisitionDTO assetAcquisitionDTO) throws URISyntaxException;

    /**
     * {@code PUT  /asset-acquisitions} : Updates an existing assetAcquisition.
     *
     * @param assetAcquisitionDTO the assetAcquisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetAcquisitionDTO,
     * or with status {@code 400 (Bad Request)} if the assetAcquisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetAcquisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    ResponseEntity<AssetAcquisitionDTO> updateAssetAcquisition(AssetAcquisitionDTO assetAcquisitionDTO) throws URISyntaxException;

    /**
     * {@code GET  /asset-acquisitions} : get all the assetAcquisitions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetAcquisitions in body.
     */
    ResponseEntity<List<AssetAcquisitionDTO>> getAllAssetAcquisitions(AssetAcquisitionCriteria criteria, Pageable pageable, MultiValueMap<String, String> queryParams,
                                                                      UriComponentsBuilder uriBuilder);

    /**
    * {@code GET  /asset-acquisitions/count} : count all the assetAcquisitions.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    ResponseEntity<Long> countAssetAcquisitions(AssetAcquisitionCriteria criteria);

    /**
     * {@code GET  /asset-acquisitions/:id} : get the "id" assetAcquisition.
     *
     * @param id the id of the assetAcquisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetAcquisitionDTO, or with status {@code 404 (Not Found)}.
     */
    ResponseEntity<AssetAcquisitionDTO> getAssetAcquisition(Long id);

    /**
     * {@code DELETE  /asset-acquisitions/:id} : delete the "id" assetAcquisition.
     *
     * @param id the id of the assetAcquisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    ResponseEntity<Void> deleteAssetAcquisition(Long id);

    /**
     * {@code SEARCH  /_search/asset-acquisitions?query=:query} : search for the assetAcquisition corresponding
     * to the query.
     *
     * @param query the query of the assetAcquisition search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    ResponseEntity<List<AssetAcquisitionDTO>> searchAssetAcquisitions(String query, Pageable pageable, MultiValueMap<String, String> queryParams,
                                                                      UriComponentsBuilder uriBuilder);
}
