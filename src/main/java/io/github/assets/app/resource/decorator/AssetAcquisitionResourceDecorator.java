package io.github.assets.app.resource.decorator;

import io.github.assets.service.dto.AssetAcquisitionCriteria;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import io.github.assets.web.rest.AssetAcquisitionResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing {@link io.github.assets.domain.AssetAcquisition}.
 */
@Component("assetAcquisitionResourceDecorator")
public class AssetAcquisitionResourceDecorator implements IAssetAcquisitionResource {

    private final AssetAcquisitionResource assetAcquisitionResource;

    public AssetAcquisitionResourceDecorator(final AssetAcquisitionResource assetAcquisitionResource) {
        this.assetAcquisitionResource = assetAcquisitionResource;
    }

    /**
     * {@code POST  /asset-acquisitions} : Create a new assetAcquisition.
     *
     * @param assetAcquisitionDTO the assetAcquisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetAcquisitionDTO, or with status {@code 400 (Bad Request)} if the assetAcquisition has already an
     * ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    @PostMapping("/asset-acquisitions")
    public ResponseEntity<AssetAcquisitionDTO> createAssetAcquisition(@Valid @RequestBody AssetAcquisitionDTO assetAcquisitionDTO) throws URISyntaxException {

        return assetAcquisitionResource.createAssetAcquisition(assetAcquisitionDTO);
    }

    /**
     * {@code PUT  /asset-acquisitions} : Updates an existing assetAcquisition.
     *
     * @param assetAcquisitionDTO the assetAcquisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetAcquisitionDTO, or with status {@code 400 (Bad Request)} if the assetAcquisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetAcquisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    @PutMapping("/asset-acquisitions")
    public ResponseEntity<AssetAcquisitionDTO> updateAssetAcquisition(@Valid @RequestBody AssetAcquisitionDTO assetAcquisitionDTO) throws URISyntaxException {

        return assetAcquisitionResource.updateAssetAcquisition(assetAcquisitionDTO);
    }

    /**
     * {@code GET  /asset-acquisitions} : get all the assetAcquisitions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetAcquisitions in body.
     */
    @Override
    @GetMapping("/asset-acquisitions")
    public ResponseEntity<List<AssetAcquisitionDTO>> getAllAssetAcquisitions(AssetAcquisitionCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams,
                                                                             UriComponentsBuilder uriBuilder) {

//        return assetAcquisitionResource.getAllAssetAcquisitions(criteria, pageable, queryParams, uriBuilder);
        return assetAcquisitionResource.getAllAssetAcquisitions(criteria, pageable);
    }

    /**
     * {@code GET  /asset-acquisitions/count} : count all the assetAcquisitions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @Override
    @GetMapping("/asset-acquisitions/count")
    public ResponseEntity<Long> countAssetAcquisitions(AssetAcquisitionCriteria criteria) {

        return assetAcquisitionResource.countAssetAcquisitions(criteria);
    }

    /**
     * {@code GET  /asset-acquisitions/:id} : get the "id" assetAcquisition.
     *
     * @param id the id of the assetAcquisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetAcquisitionDTO, or with status {@code 404 (Not Found)}.
     */
    @Override
    @GetMapping("/asset-acquisitions/{id}")
    public ResponseEntity<AssetAcquisitionDTO> getAssetAcquisition(@PathVariable Long id) {

        return assetAcquisitionResource.getAssetAcquisition(id);
    }

    /**
     * {@code DELETE  /asset-acquisitions/:id} : delete the "id" assetAcquisition.
     *
     * @param id the id of the assetAcquisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Override
    @DeleteMapping("/asset-acquisitions/{id}")
    public ResponseEntity<Void> deleteAssetAcquisition(@PathVariable Long id) {
        return assetAcquisitionResource.deleteAssetAcquisition(id);
    }

    /**
     * {@code SEARCH  /_search/asset-acquisitions?query=:query} : search for the assetAcquisition corresponding to the query.
     *
     * @param query    the query of the assetAcquisition search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @Override
    @GetMapping("/_search/asset-acquisitions")
    public ResponseEntity<List<AssetAcquisitionDTO>> searchAssetAcquisitions(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams,
                                                                             UriComponentsBuilder uriBuilder) {

//        return assetAcquisitionResource.searchAssetAcquisitions(query, pageable, queryParams, uriBuilder);
        return assetAcquisitionResource.searchAssetAcquisitions(query, pageable);
    }

}
