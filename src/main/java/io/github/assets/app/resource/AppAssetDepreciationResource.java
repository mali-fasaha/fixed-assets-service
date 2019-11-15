package io.github.assets.app.resource;

import io.github.assets.app.messaging.MutationResource;
import io.github.assets.app.resource.decorator.IAssetDepreciationResource;
import io.github.assets.service.dto.AssetDepreciationCriteria;
import io.github.assets.service.dto.AssetDepreciationDTO;
import io.github.assets.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

@Slf4j
@RestController
@RequestMapping("/api/app")
public class AppAssetDepreciationResource implements IAssetDepreciationResource {

    private static final String ENTITY_NAME = "fixedAssetServiceAssetDepreciation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IAssetDepreciationResource assetDepreciationResourceDecorator;

    private final MutationResource<AssetDepreciationDTO> assetDepreciationMutationResource;

    public AppAssetDepreciationResource(final IAssetDepreciationResource assetDepreciationResourceDecorator, final MutationResource<AssetDepreciationDTO> assetDepreciationMutationResource) {
        this.assetDepreciationResourceDecorator = assetDepreciationResourceDecorator;
        this.assetDepreciationMutationResource = assetDepreciationMutationResource;
    }

    /**
     * {@code POST  /asset-depreciations} : Create a new assetDepreciation.
     *
     * @param assetDepreciationDTO the assetDepreciationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetDepreciationDTO, or with status {@code 400 (Bad Request)} if the assetDepreciation has already an
     * ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-depreciations")
    public ResponseEntity<AssetDepreciationDTO> createAssetDepreciation(@Valid @RequestBody AssetDepreciationDTO assetDepreciationDTO) throws URISyntaxException {
        log.debug("REST request to save AssetDepreciation : {}", assetDepreciationDTO);
        if (assetDepreciationDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetDepreciation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        assetDepreciationMutationResource.createEntity(assetDepreciationDTO);
        return ResponseEntity.ok(assetDepreciationDTO);
    }

    /**
     * {@code PUT  /asset-depreciations} : Updates an existing assetDepreciation.
     *
     * @param assetDepreciationDTO the assetDepreciationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetDepreciationDTO, or with status {@code 400 (Bad Request)} if the assetDepreciationDTO is not
     * valid, or with status {@code 500 (Internal Server Error)} if the assetDepreciationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-depreciations")
    public ResponseEntity<AssetDepreciationDTO> updateAssetDepreciation(@Valid @RequestBody AssetDepreciationDTO assetDepreciationDTO) throws URISyntaxException {
        log.debug("REST request to update AssetDepreciation : {}", assetDepreciationDTO);
        if (assetDepreciationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        assetDepreciationMutationResource.updateEntity(assetDepreciationDTO);
        return ResponseEntity.ok(assetDepreciationDTO);
    }

    /**
     * {@code GET  /asset-depreciations} : get all the assetDepreciations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetDepreciations in body.
     */
    @GetMapping("/asset-depreciations")
    public ResponseEntity<List<AssetDepreciationDTO>> getAllAssetDepreciations(AssetDepreciationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AssetDepreciations by criteria: {}", criteria);
        return assetDepreciationResourceDecorator.getAllAssetDepreciations(criteria, pageable);
    }

    /**
     * {@code GET  /asset-depreciations/count} : count all the assetDepreciations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/asset-depreciations/count")
    public ResponseEntity<Long> countAssetDepreciations(AssetDepreciationCriteria criteria) {
        log.debug("REST request to count AssetDepreciations by criteria: {}", criteria);
        return assetDepreciationResourceDecorator.countAssetDepreciations(criteria);
    }

    /**
     * {@code GET  /asset-depreciations/:id} : get the "id" assetDepreciation.
     *
     * @param id the id of the assetDepreciationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetDepreciationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-depreciations/{id}")
    public ResponseEntity<AssetDepreciationDTO> getAssetDepreciation(@PathVariable Long id) {
        log.debug("REST request to get AssetDepreciation : {}", id);
        return assetDepreciationResourceDecorator.getAssetDepreciation(id);
    }

    /**
     * {@code DELETE  /asset-depreciations/:id} : delete the "id" assetDepreciation.
     *
     * @param id the id of the assetDepreciationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-depreciations/{id}")
    public ResponseEntity<Void> deleteAssetDepreciation(@PathVariable Long id) {
        log.debug("REST request to delete AssetDepreciation : {}", id);
        assetDepreciationMutationResource.deleteEntity(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
