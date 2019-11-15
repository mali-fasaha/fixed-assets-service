package io.github.assets.web.rest;

import io.github.assets.FixedAssetServiceApp;
import io.github.assets.config.SecurityBeanOverrideConfiguration;
import io.github.assets.domain.DepreciationRegime;
import io.github.assets.repository.DepreciationRegimeRepository;
import io.github.assets.service.DepreciationRegimeService;
import io.github.assets.service.dto.DepreciationRegimeDTO;
import io.github.assets.service.mapper.DepreciationRegimeMapper;
import io.github.assets.web.rest.errors.ExceptionTranslator;
import io.github.assets.service.dto.DepreciationRegimeCriteria;
import io.github.assets.service.DepreciationRegimeQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static io.github.assets.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.assets.domain.enumeration.AssetDecayType;
/**
 * Integration tests for the {@link DepreciationRegimeResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, FixedAssetServiceApp.class})
public class DepreciationRegimeResourceIT {

    private static final AssetDecayType DEFAULT_ASSET_DECAY_TYPE = AssetDecayType.STRAIGHT_LINE;
    private static final AssetDecayType UPDATED_ASSET_DECAY_TYPE = AssetDecayType.DECLINING_BALANCE;

    private static final Double DEFAULT_DEPRECIATION_RATE = 1D;
    private static final Double UPDATED_DEPRECIATION_RATE = 2D;
    private static final Double SMALLER_DEPRECIATION_RATE = 1D - 1D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private DepreciationRegimeRepository depreciationRegimeRepository;

    @Autowired
    private DepreciationRegimeMapper depreciationRegimeMapper;

    @Autowired
    private DepreciationRegimeService depreciationRegimeService;

    @Autowired
    private DepreciationRegimeQueryService depreciationRegimeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDepreciationRegimeMockMvc;

    private DepreciationRegime depreciationRegime;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DepreciationRegimeResource depreciationRegimeResource = new DepreciationRegimeResource(depreciationRegimeService, depreciationRegimeQueryService);
        this.restDepreciationRegimeMockMvc = MockMvcBuilders.standaloneSetup(depreciationRegimeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationRegime createEntity(EntityManager em) {
        DepreciationRegime depreciationRegime = new DepreciationRegime()
            .assetDecayType(DEFAULT_ASSET_DECAY_TYPE)
            .depreciationRate(DEFAULT_DEPRECIATION_RATE)
            .description(DEFAULT_DESCRIPTION);
        return depreciationRegime;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationRegime createUpdatedEntity(EntityManager em) {
        DepreciationRegime depreciationRegime = new DepreciationRegime()
            .assetDecayType(UPDATED_ASSET_DECAY_TYPE)
            .depreciationRate(UPDATED_DEPRECIATION_RATE)
            .description(UPDATED_DESCRIPTION);
        return depreciationRegime;
    }

    @BeforeEach
    public void initTest() {
        depreciationRegime = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepreciationRegime() throws Exception {
        int databaseSizeBeforeCreate = depreciationRegimeRepository.findAll().size();

        // Create the DepreciationRegime
        DepreciationRegimeDTO depreciationRegimeDTO = depreciationRegimeMapper.toDto(depreciationRegime);
        restDepreciationRegimeMockMvc.perform(post("/api/depreciation-regimes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depreciationRegimeDTO)))
            .andExpect(status().isCreated());

        // Validate the DepreciationRegime in the database
        List<DepreciationRegime> depreciationRegimeList = depreciationRegimeRepository.findAll();
        assertThat(depreciationRegimeList).hasSize(databaseSizeBeforeCreate + 1);
        DepreciationRegime testDepreciationRegime = depreciationRegimeList.get(depreciationRegimeList.size() - 1);
        assertThat(testDepreciationRegime.getAssetDecayType()).isEqualTo(DEFAULT_ASSET_DECAY_TYPE);
        assertThat(testDepreciationRegime.getDepreciationRate()).isEqualTo(DEFAULT_DEPRECIATION_RATE);
        assertThat(testDepreciationRegime.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDepreciationRegimeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = depreciationRegimeRepository.findAll().size();

        // Create the DepreciationRegime with an existing ID
        depreciationRegime.setId(1L);
        DepreciationRegimeDTO depreciationRegimeDTO = depreciationRegimeMapper.toDto(depreciationRegime);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepreciationRegimeMockMvc.perform(post("/api/depreciation-regimes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depreciationRegimeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DepreciationRegime in the database
        List<DepreciationRegime> depreciationRegimeList = depreciationRegimeRepository.findAll();
        assertThat(depreciationRegimeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkAssetDecayTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = depreciationRegimeRepository.findAll().size();
        // set the field null
        depreciationRegime.setAssetDecayType(null);

        // Create the DepreciationRegime, which fails.
        DepreciationRegimeDTO depreciationRegimeDTO = depreciationRegimeMapper.toDto(depreciationRegime);

        restDepreciationRegimeMockMvc.perform(post("/api/depreciation-regimes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depreciationRegimeDTO)))
            .andExpect(status().isBadRequest());

        List<DepreciationRegime> depreciationRegimeList = depreciationRegimeRepository.findAll();
        assertThat(depreciationRegimeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDepreciationRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = depreciationRegimeRepository.findAll().size();
        // set the field null
        depreciationRegime.setDepreciationRate(null);

        // Create the DepreciationRegime, which fails.
        DepreciationRegimeDTO depreciationRegimeDTO = depreciationRegimeMapper.toDto(depreciationRegime);

        restDepreciationRegimeMockMvc.perform(post("/api/depreciation-regimes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depreciationRegimeDTO)))
            .andExpect(status().isBadRequest());

        List<DepreciationRegime> depreciationRegimeList = depreciationRegimeRepository.findAll();
        assertThat(depreciationRegimeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDepreciationRegimes() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList
        restDepreciationRegimeMockMvc.perform(get("/api/depreciation-regimes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationRegime.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetDecayType").value(hasItem(DEFAULT_ASSET_DECAY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].depreciationRate").value(hasItem(DEFAULT_DEPRECIATION_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getDepreciationRegime() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get the depreciationRegime
        restDepreciationRegimeMockMvc.perform(get("/api/depreciation-regimes/{id}", depreciationRegime.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(depreciationRegime.getId().intValue()))
            .andExpect(jsonPath("$.assetDecayType").value(DEFAULT_ASSET_DECAY_TYPE.toString()))
            .andExpect(jsonPath("$.depreciationRate").value(DEFAULT_DEPRECIATION_RATE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getDepreciationRegimesByIdFiltering() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        Long id = depreciationRegime.getId();

        defaultDepreciationRegimeShouldBeFound("id.equals=" + id);
        defaultDepreciationRegimeShouldNotBeFound("id.notEquals=" + id);

        defaultDepreciationRegimeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepreciationRegimeShouldNotBeFound("id.greaterThan=" + id);

        defaultDepreciationRegimeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepreciationRegimeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDepreciationRegimesByAssetDecayTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where assetDecayType equals to DEFAULT_ASSET_DECAY_TYPE
        defaultDepreciationRegimeShouldBeFound("assetDecayType.equals=" + DEFAULT_ASSET_DECAY_TYPE);

        // Get all the depreciationRegimeList where assetDecayType equals to UPDATED_ASSET_DECAY_TYPE
        defaultDepreciationRegimeShouldNotBeFound("assetDecayType.equals=" + UPDATED_ASSET_DECAY_TYPE);
    }

    @Test
    @Transactional
    public void getAllDepreciationRegimesByAssetDecayTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where assetDecayType not equals to DEFAULT_ASSET_DECAY_TYPE
        defaultDepreciationRegimeShouldNotBeFound("assetDecayType.notEquals=" + DEFAULT_ASSET_DECAY_TYPE);

        // Get all the depreciationRegimeList where assetDecayType not equals to UPDATED_ASSET_DECAY_TYPE
        defaultDepreciationRegimeShouldBeFound("assetDecayType.notEquals=" + UPDATED_ASSET_DECAY_TYPE);
    }

    @Test
    @Transactional
    public void getAllDepreciationRegimesByAssetDecayTypeIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where assetDecayType in DEFAULT_ASSET_DECAY_TYPE or UPDATED_ASSET_DECAY_TYPE
        defaultDepreciationRegimeShouldBeFound("assetDecayType.in=" + DEFAULT_ASSET_DECAY_TYPE + "," + UPDATED_ASSET_DECAY_TYPE);

        // Get all the depreciationRegimeList where assetDecayType equals to UPDATED_ASSET_DECAY_TYPE
        defaultDepreciationRegimeShouldNotBeFound("assetDecayType.in=" + UPDATED_ASSET_DECAY_TYPE);
    }

    @Test
    @Transactional
    public void getAllDepreciationRegimesByAssetDecayTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where assetDecayType is not null
        defaultDepreciationRegimeShouldBeFound("assetDecayType.specified=true");

        // Get all the depreciationRegimeList where assetDecayType is null
        defaultDepreciationRegimeShouldNotBeFound("assetDecayType.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepreciationRegimesByDepreciationRateIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where depreciationRate equals to DEFAULT_DEPRECIATION_RATE
        defaultDepreciationRegimeShouldBeFound("depreciationRate.equals=" + DEFAULT_DEPRECIATION_RATE);

        // Get all the depreciationRegimeList where depreciationRate equals to UPDATED_DEPRECIATION_RATE
        defaultDepreciationRegimeShouldNotBeFound("depreciationRate.equals=" + UPDATED_DEPRECIATION_RATE);
    }

    @Test
    @Transactional
    public void getAllDepreciationRegimesByDepreciationRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where depreciationRate not equals to DEFAULT_DEPRECIATION_RATE
        defaultDepreciationRegimeShouldNotBeFound("depreciationRate.notEquals=" + DEFAULT_DEPRECIATION_RATE);

        // Get all the depreciationRegimeList where depreciationRate not equals to UPDATED_DEPRECIATION_RATE
        defaultDepreciationRegimeShouldBeFound("depreciationRate.notEquals=" + UPDATED_DEPRECIATION_RATE);
    }

    @Test
    @Transactional
    public void getAllDepreciationRegimesByDepreciationRateIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where depreciationRate in DEFAULT_DEPRECIATION_RATE or UPDATED_DEPRECIATION_RATE
        defaultDepreciationRegimeShouldBeFound("depreciationRate.in=" + DEFAULT_DEPRECIATION_RATE + "," + UPDATED_DEPRECIATION_RATE);

        // Get all the depreciationRegimeList where depreciationRate equals to UPDATED_DEPRECIATION_RATE
        defaultDepreciationRegimeShouldNotBeFound("depreciationRate.in=" + UPDATED_DEPRECIATION_RATE);
    }

    @Test
    @Transactional
    public void getAllDepreciationRegimesByDepreciationRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where depreciationRate is not null
        defaultDepreciationRegimeShouldBeFound("depreciationRate.specified=true");

        // Get all the depreciationRegimeList where depreciationRate is null
        defaultDepreciationRegimeShouldNotBeFound("depreciationRate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepreciationRegimesByDepreciationRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where depreciationRate is greater than or equal to DEFAULT_DEPRECIATION_RATE
        defaultDepreciationRegimeShouldBeFound("depreciationRate.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_RATE);

        // Get all the depreciationRegimeList where depreciationRate is greater than or equal to UPDATED_DEPRECIATION_RATE
        defaultDepreciationRegimeShouldNotBeFound("depreciationRate.greaterThanOrEqual=" + UPDATED_DEPRECIATION_RATE);
    }

    @Test
    @Transactional
    public void getAllDepreciationRegimesByDepreciationRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where depreciationRate is less than or equal to DEFAULT_DEPRECIATION_RATE
        defaultDepreciationRegimeShouldBeFound("depreciationRate.lessThanOrEqual=" + DEFAULT_DEPRECIATION_RATE);

        // Get all the depreciationRegimeList where depreciationRate is less than or equal to SMALLER_DEPRECIATION_RATE
        defaultDepreciationRegimeShouldNotBeFound("depreciationRate.lessThanOrEqual=" + SMALLER_DEPRECIATION_RATE);
    }

    @Test
    @Transactional
    public void getAllDepreciationRegimesByDepreciationRateIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where depreciationRate is less than DEFAULT_DEPRECIATION_RATE
        defaultDepreciationRegimeShouldNotBeFound("depreciationRate.lessThan=" + DEFAULT_DEPRECIATION_RATE);

        // Get all the depreciationRegimeList where depreciationRate is less than UPDATED_DEPRECIATION_RATE
        defaultDepreciationRegimeShouldBeFound("depreciationRate.lessThan=" + UPDATED_DEPRECIATION_RATE);
    }

    @Test
    @Transactional
    public void getAllDepreciationRegimesByDepreciationRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where depreciationRate is greater than DEFAULT_DEPRECIATION_RATE
        defaultDepreciationRegimeShouldNotBeFound("depreciationRate.greaterThan=" + DEFAULT_DEPRECIATION_RATE);

        // Get all the depreciationRegimeList where depreciationRate is greater than SMALLER_DEPRECIATION_RATE
        defaultDepreciationRegimeShouldBeFound("depreciationRate.greaterThan=" + SMALLER_DEPRECIATION_RATE);
    }


    @Test
    @Transactional
    public void getAllDepreciationRegimesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where description equals to DEFAULT_DESCRIPTION
        defaultDepreciationRegimeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the depreciationRegimeList where description equals to UPDATED_DESCRIPTION
        defaultDepreciationRegimeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDepreciationRegimesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where description not equals to DEFAULT_DESCRIPTION
        defaultDepreciationRegimeShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the depreciationRegimeList where description not equals to UPDATED_DESCRIPTION
        defaultDepreciationRegimeShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDepreciationRegimesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDepreciationRegimeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the depreciationRegimeList where description equals to UPDATED_DESCRIPTION
        defaultDepreciationRegimeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDepreciationRegimesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where description is not null
        defaultDepreciationRegimeShouldBeFound("description.specified=true");

        // Get all the depreciationRegimeList where description is null
        defaultDepreciationRegimeShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllDepreciationRegimesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where description contains DEFAULT_DESCRIPTION
        defaultDepreciationRegimeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the depreciationRegimeList where description contains UPDATED_DESCRIPTION
        defaultDepreciationRegimeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDepreciationRegimesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        // Get all the depreciationRegimeList where description does not contain DEFAULT_DESCRIPTION
        defaultDepreciationRegimeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the depreciationRegimeList where description does not contain UPDATED_DESCRIPTION
        defaultDepreciationRegimeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepreciationRegimeShouldBeFound(String filter) throws Exception {
        restDepreciationRegimeMockMvc.perform(get("/api/depreciation-regimes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationRegime.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetDecayType").value(hasItem(DEFAULT_ASSET_DECAY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].depreciationRate").value(hasItem(DEFAULT_DEPRECIATION_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restDepreciationRegimeMockMvc.perform(get("/api/depreciation-regimes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepreciationRegimeShouldNotBeFound(String filter) throws Exception {
        restDepreciationRegimeMockMvc.perform(get("/api/depreciation-regimes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepreciationRegimeMockMvc.perform(get("/api/depreciation-regimes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDepreciationRegime() throws Exception {
        // Get the depreciationRegime
        restDepreciationRegimeMockMvc.perform(get("/api/depreciation-regimes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepreciationRegime() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        int databaseSizeBeforeUpdate = depreciationRegimeRepository.findAll().size();

        // Update the depreciationRegime
        DepreciationRegime updatedDepreciationRegime = depreciationRegimeRepository.findById(depreciationRegime.getId()).get();
        // Disconnect from session so that the updates on updatedDepreciationRegime are not directly saved in db
        em.detach(updatedDepreciationRegime);
        updatedDepreciationRegime
            .assetDecayType(UPDATED_ASSET_DECAY_TYPE)
            .depreciationRate(UPDATED_DEPRECIATION_RATE)
            .description(UPDATED_DESCRIPTION);
        DepreciationRegimeDTO depreciationRegimeDTO = depreciationRegimeMapper.toDto(updatedDepreciationRegime);

        restDepreciationRegimeMockMvc.perform(put("/api/depreciation-regimes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depreciationRegimeDTO)))
            .andExpect(status().isOk());

        // Validate the DepreciationRegime in the database
        List<DepreciationRegime> depreciationRegimeList = depreciationRegimeRepository.findAll();
        assertThat(depreciationRegimeList).hasSize(databaseSizeBeforeUpdate);
        DepreciationRegime testDepreciationRegime = depreciationRegimeList.get(depreciationRegimeList.size() - 1);
        assertThat(testDepreciationRegime.getAssetDecayType()).isEqualTo(UPDATED_ASSET_DECAY_TYPE);
        assertThat(testDepreciationRegime.getDepreciationRate()).isEqualTo(UPDATED_DEPRECIATION_RATE);
        assertThat(testDepreciationRegime.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingDepreciationRegime() throws Exception {
        int databaseSizeBeforeUpdate = depreciationRegimeRepository.findAll().size();

        // Create the DepreciationRegime
        DepreciationRegimeDTO depreciationRegimeDTO = depreciationRegimeMapper.toDto(depreciationRegime);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationRegimeMockMvc.perform(put("/api/depreciation-regimes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depreciationRegimeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DepreciationRegime in the database
        List<DepreciationRegime> depreciationRegimeList = depreciationRegimeRepository.findAll();
        assertThat(depreciationRegimeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDepreciationRegime() throws Exception {
        // Initialize the database
        depreciationRegimeRepository.saveAndFlush(depreciationRegime);

        int databaseSizeBeforeDelete = depreciationRegimeRepository.findAll().size();

        // Delete the depreciationRegime
        restDepreciationRegimeMockMvc.perform(delete("/api/depreciation-regimes/{id}", depreciationRegime.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepreciationRegime> depreciationRegimeList = depreciationRegimeRepository.findAll();
        assertThat(depreciationRegimeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
