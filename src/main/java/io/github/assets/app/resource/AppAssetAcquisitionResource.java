package io.github.assets.app.resource;

import io.github.assets.app.messaging.MutationResource;
import io.github.assets.app.resource.decorator.IAssetAcquisitionResource;
import io.github.assets.service.dto.AssetAcquisitionCriteria;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import io.github.assets.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
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
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing {@link io.github.assets.domain.AssetAcquisition}.
 */
@RestController
@RequestMapping("/api/app")
public class AppAssetAcquisitionResource implements IAssetAcquisitionResource {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IAssetAcquisitionResource assetAcquisitionResourceDecorator;
    private static final String ENTITY_NAME = "fixedAssetServiceAssetAcquisition";

    // TODO Something awesome with this interface
    private final MutationResource<AssetAcquisitionDTO> assetAcquisitionMutationResource;

    public AppAssetAcquisitionResource(final IAssetAcquisitionResource assetAcquisitionResourceDecorator, final MutationResource<AssetAcquisitionDTO> assetAcquisitionMutationResource) {
        this.assetAcquisitionResourceDecorator = assetAcquisitionResourceDecorator;
        this.assetAcquisitionMutationResource = assetAcquisitionMutationResource;
    }

    /**
     * {@code POST  /asset-acquisitions} : Create a new assetAcquisition.
     *
     * @param assetAcquisitionDTO the assetAcquisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetAcquisitionDTO, or with status {@code 400 (Bad Request)} if the assetAcquisition has already an
     * ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-acquisitions")
    public ResponseEntity<AssetAcquisitionDTO> createAssetAcquisition(@Valid @RequestBody AssetAcquisitionDTO assetAcquisitionDTO) throws URISyntaxException {

        if (assetAcquisitionDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetAcquisition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        assetAcquisitionMutationResource.createEntity(assetAcquisitionDTO);

        return ResponseEntity.ok(assetAcquisitionDTO);
    }

    /**
     * {@code PUT  /asset-acquisitions} : Updates an existing assetAcquisition.
     *
     * @param assetAcquisitionDTO the assetAcquisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetAcquisitionDTO, or with status {@code 400 (Bad Request)} if the assetAcquisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetAcquisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-acquisitions")
    public ResponseEntity<AssetAcquisitionDTO> updateAssetAcquisition(@Valid @RequestBody AssetAcquisitionDTO assetAcquisitionDTO) throws URISyntaxException {

        if (assetAcquisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        assetAcquisitionMutationResource.updateEntity(assetAcquisitionDTO);

        return ResponseEntity.ok(assetAcquisitionDTO);
    }

    /**
     * {@code GET  /asset-acquisitions} : get all the assetAcquisitions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetAcquisitions in body.
     */
    @GetMapping("/asset-acquisitions")
    public ResponseEntity<List<AssetAcquisitionDTO>> getAllAssetAcquisitions(AssetAcquisitionCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams,
                                                                             UriComponentsBuilder uriBuilder) {

        return assetAcquisitionResourceDecorator.getAllAssetAcquisitions(criteria, pageable, queryParams, uriBuilder);
    }

    /**
     * {@code GET  /asset-acquisitions/count} : count all the assetAcquisitions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/asset-acquisitions/count")
    public ResponseEntity<Long> countAssetAcquisitions(AssetAcquisitionCriteria criteria) {

        return assetAcquisitionResourceDecorator.countAssetAcquisitions(criteria);
    }

    /**
     * {@code GET  /asset-acquisitions/:id} : get the "id" assetAcquisition.
     *
     * @param id the id of the assetAcquisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetAcquisitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-acquisitions/{id}")
    public ResponseEntity<AssetAcquisitionDTO> getAssetAcquisition(@PathVariable Long id) {

        return assetAcquisitionResourceDecorator.getAssetAcquisition(id);
    }

    /**
     * {@code DELETE  /asset-acquisitions/:id} : delete the "id" assetAcquisition.
     *
     * @param id the id of the assetAcquisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-acquisitions/{id}")
    public ResponseEntity<Void> deleteAssetAcquisition(@PathVariable Long id) {

        assetAcquisitionMutationResource.deleteEntity(id);

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
