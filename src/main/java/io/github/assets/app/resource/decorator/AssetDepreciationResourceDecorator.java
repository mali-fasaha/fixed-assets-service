package io.github.assets.app.resource.decorator;

import io.github.assets.service.dto.AssetDepreciationCriteria;
import io.github.assets.service.dto.AssetDepreciationDTO;
import io.github.assets.web.rest.AssetDepreciationResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@Component("assetDepreciationResourceDecorator")
public class AssetDepreciationResourceDecorator implements IAssetDepreciationResource {

    private final AssetDepreciationResource assetDepreciationResource;

    public AssetDepreciationResourceDecorator(final AssetDepreciationResource assetDepreciationResource) {
        this.assetDepreciationResource = assetDepreciationResource;
    }

    /**
     * {@code POST  /asset-depreciations} : Create a new assetDepreciation.
     *
     * @param assetDepreciationDTO the assetDepreciationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetDepreciationDTO, or with status {@code 400 (Bad Request)} if the assetDepreciation has already an
     * ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    public ResponseEntity<AssetDepreciationDTO> createAssetDepreciation(@Valid final AssetDepreciationDTO assetDepreciationDTO) throws URISyntaxException {
        return assetDepreciationResource.createAssetDepreciation(assetDepreciationDTO);
    }

    /**
     * {@code PUT  /asset-depreciations} : Updates an existing assetDepreciation.
     *
     * @param assetDepreciationDTO the assetDepreciationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetDepreciationDTO, or with status {@code 400 (Bad Request)} if the assetDepreciationDTO is not
     * valid, or with status {@code 500 (Internal Server Error)} if the assetDepreciationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    public ResponseEntity<AssetDepreciationDTO> updateAssetDepreciation(@Valid final AssetDepreciationDTO assetDepreciationDTO) throws URISyntaxException {
        return assetDepreciationResource.updateAssetDepreciation(assetDepreciationDTO);
    }

    /**
     * {@code GET  /asset-depreciations} : get all the assetDepreciations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetDepreciations in body.
     */
    @Override
    public ResponseEntity<List<AssetDepreciationDTO>> getAllAssetDepreciations(final AssetDepreciationCriteria criteria, final Pageable pageable) {
        return assetDepreciationResource.getAllAssetDepreciations(criteria, pageable);
    }

    /**
     * {@code GET  /asset-depreciations/count} : count all the assetDepreciations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @Override
    public ResponseEntity<Long> countAssetDepreciations(final AssetDepreciationCriteria criteria) {
        return assetDepreciationResource.countAssetDepreciations(criteria);
    }

    /**
     * {@code GET  /asset-depreciations/:id} : get the "id" assetDepreciation.
     *
     * @param id the id of the assetDepreciationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetDepreciationDTO, or with status {@code 404 (Not Found)}.
     */
    @Override
    public ResponseEntity<AssetDepreciationDTO> getAssetDepreciation(final Long id) {
        return assetDepreciationResource.getAssetDepreciation(id);
    }

    /**
     * {@code DELETE  /asset-depreciations/:id} : delete the "id" assetDepreciation.
     *
     * @param id the id of the assetDepreciationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Override
    public ResponseEntity<Void> deleteAssetDepreciation(final Long id) {
        return assetDepreciationResource.deleteAssetDepreciation(id);
    }

    /**
     * {@code SEARCH  /_search/asset-depreciations?query=:query} : search for the assetDepreciation corresponding to the query.
     *
     * @param query    the query of the assetDepreciation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @Override
    public ResponseEntity<List<AssetDepreciationDTO>> searchAssetDepreciations(final String query, final Pageable pageable) {
        return assetDepreciationResource.searchAssetDepreciations(query, pageable);
    }
}
