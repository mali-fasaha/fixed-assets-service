package io.github.assets.web.rest;

import io.github.assets.FixedAssetServiceApp;
import io.github.assets.config.SecurityBeanOverrideConfiguration;
import io.github.assets.domain.AssetAcquisition;
import io.github.assets.repository.AssetAcquisitionRepository;
import io.github.assets.repository.search.AssetAcquisitionSearchRepository;
import io.github.assets.service.AssetAcquisitionService;
import io.github.assets.service.dto.AssetAcquisitionDTO;
import io.github.assets.service.mapper.AssetAcquisitionMapper;
import io.github.assets.web.rest.errors.ExceptionTranslator;
import io.github.assets.service.dto.AssetAcquisitionCriteria;
import io.github.assets.service.AssetAcquisitionQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static io.github.assets.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AssetAcquisitionResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, FixedAssetServiceApp.class})
public class AssetAcquisitionResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ACQUISITION_MONTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACQUISITION_MONTH = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ACQUISITION_MONTH = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ASSET_SERIAL = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_SERIAL = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_OUTLET_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_OUTLET_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_ACQUISITION_TRANSACTION_ID = 1L;
    private static final Long UPDATED_ACQUISITION_TRANSACTION_ID = 2L;
    private static final Long SMALLER_ACQUISITION_TRANSACTION_ID = 1L - 1L;

    private static final Long DEFAULT_ASSET_CATEGORY_ID = 1L;
    private static final Long UPDATED_ASSET_CATEGORY_ID = 2L;
    private static final Long SMALLER_ASSET_CATEGORY_ID = 1L - 1L;

    private static final BigDecimal DEFAULT_PURCHASE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PURCHASE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PURCHASE_AMOUNT = new BigDecimal(1 - 1);

    private static final Long DEFAULT_ASSET_DEALER_ID = 1L;
    private static final Long UPDATED_ASSET_DEALER_ID = 2L;
    private static final Long SMALLER_ASSET_DEALER_ID = 1L - 1L;

    private static final Long DEFAULT_ASSET_INVOICE_ID = 1L;
    private static final Long UPDATED_ASSET_INVOICE_ID = 2L;
    private static final Long SMALLER_ASSET_INVOICE_ID = 1L - 1L;

    @Autowired
    private AssetAcquisitionRepository assetAcquisitionRepository;

    @Autowired
    private AssetAcquisitionMapper assetAcquisitionMapper;

    @Autowired
    private AssetAcquisitionService assetAcquisitionService;

    /**
     * This repository is mocked in the io.github.assets.repository.search test package.
     *
     * @see io.github.assets.repository.search.AssetAcquisitionSearchRepositoryMockConfiguration
     */
    @Autowired
    private AssetAcquisitionSearchRepository mockAssetAcquisitionSearchRepository;

    @Autowired
    private AssetAcquisitionQueryService assetAcquisitionQueryService;

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

    private MockMvc restAssetAcquisitionMockMvc;

    private AssetAcquisition assetAcquisition;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AssetAcquisitionResource assetAcquisitionResource = new AssetAcquisitionResource(assetAcquisitionService, assetAcquisitionQueryService);
        this.restAssetAcquisitionMockMvc = MockMvcBuilders.standaloneSetup(assetAcquisitionResource)
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
    public static AssetAcquisition createEntity(EntityManager em) {
        AssetAcquisition assetAcquisition = new AssetAcquisition()
            .description(DEFAULT_DESCRIPTION)
            .acquisitionMonth(DEFAULT_ACQUISITION_MONTH)
            .assetSerial(DEFAULT_ASSET_SERIAL)
            .serviceOutletCode(DEFAULT_SERVICE_OUTLET_CODE)
            .acquisitionTransactionId(DEFAULT_ACQUISITION_TRANSACTION_ID)
            .assetCategoryId(DEFAULT_ASSET_CATEGORY_ID)
            .purchaseAmount(DEFAULT_PURCHASE_AMOUNT)
            .assetDealerId(DEFAULT_ASSET_DEALER_ID)
            .assetInvoiceId(DEFAULT_ASSET_INVOICE_ID);
        return assetAcquisition;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetAcquisition createUpdatedEntity(EntityManager em) {
        AssetAcquisition assetAcquisition = new AssetAcquisition()
            .description(UPDATED_DESCRIPTION)
            .acquisitionMonth(UPDATED_ACQUISITION_MONTH)
            .assetSerial(UPDATED_ASSET_SERIAL)
            .serviceOutletCode(UPDATED_SERVICE_OUTLET_CODE)
            .acquisitionTransactionId(UPDATED_ACQUISITION_TRANSACTION_ID)
            .assetCategoryId(UPDATED_ASSET_CATEGORY_ID)
            .purchaseAmount(UPDATED_PURCHASE_AMOUNT)
            .assetDealerId(UPDATED_ASSET_DEALER_ID)
            .assetInvoiceId(UPDATED_ASSET_INVOICE_ID);
        return assetAcquisition;
    }

    @BeforeEach
    public void initTest() {
        assetAcquisition = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssetAcquisition() throws Exception {
        int databaseSizeBeforeCreate = assetAcquisitionRepository.findAll().size();

        // Create the AssetAcquisition
        AssetAcquisitionDTO assetAcquisitionDTO = assetAcquisitionMapper.toDto(assetAcquisition);
        restAssetAcquisitionMockMvc.perform(post("/api/asset-acquisitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetAcquisitionDTO)))
            .andExpect(status().isCreated());

        // Validate the AssetAcquisition in the database
        List<AssetAcquisition> assetAcquisitionList = assetAcquisitionRepository.findAll();
        assertThat(assetAcquisitionList).hasSize(databaseSizeBeforeCreate + 1);
        AssetAcquisition testAssetAcquisition = assetAcquisitionList.get(assetAcquisitionList.size() - 1);
        assertThat(testAssetAcquisition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssetAcquisition.getAcquisitionMonth()).isEqualTo(DEFAULT_ACQUISITION_MONTH);
        assertThat(testAssetAcquisition.getAssetSerial()).isEqualTo(DEFAULT_ASSET_SERIAL);
        assertThat(testAssetAcquisition.getServiceOutletCode()).isEqualTo(DEFAULT_SERVICE_OUTLET_CODE);
        assertThat(testAssetAcquisition.getAcquisitionTransactionId()).isEqualTo(DEFAULT_ACQUISITION_TRANSACTION_ID);
        assertThat(testAssetAcquisition.getAssetCategoryId()).isEqualTo(DEFAULT_ASSET_CATEGORY_ID);
        assertThat(testAssetAcquisition.getPurchaseAmount()).isEqualTo(DEFAULT_PURCHASE_AMOUNT);
        assertThat(testAssetAcquisition.getAssetDealerId()).isEqualTo(DEFAULT_ASSET_DEALER_ID);
        assertThat(testAssetAcquisition.getAssetInvoiceId()).isEqualTo(DEFAULT_ASSET_INVOICE_ID);

        // Validate the AssetAcquisition in Elasticsearch
        verify(mockAssetAcquisitionSearchRepository, times(1)).save(testAssetAcquisition);
    }

    @Test
    @Transactional
    public void createAssetAcquisitionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = assetAcquisitionRepository.findAll().size();

        // Create the AssetAcquisition with an existing ID
        assetAcquisition.setId(1L);
        AssetAcquisitionDTO assetAcquisitionDTO = assetAcquisitionMapper.toDto(assetAcquisition);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetAcquisitionMockMvc.perform(post("/api/asset-acquisitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetAcquisitionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AssetAcquisition in the database
        List<AssetAcquisition> assetAcquisitionList = assetAcquisitionRepository.findAll();
        assertThat(assetAcquisitionList).hasSize(databaseSizeBeforeCreate);

        // Validate the AssetAcquisition in Elasticsearch
        verify(mockAssetAcquisitionSearchRepository, times(0)).save(assetAcquisition);
    }


    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetAcquisitionRepository.findAll().size();
        // set the field null
        assetAcquisition.setDescription(null);

        // Create the AssetAcquisition, which fails.
        AssetAcquisitionDTO assetAcquisitionDTO = assetAcquisitionMapper.toDto(assetAcquisition);

        restAssetAcquisitionMockMvc.perform(post("/api/asset-acquisitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetAcquisitionDTO)))
            .andExpect(status().isBadRequest());

        List<AssetAcquisition> assetAcquisitionList = assetAcquisitionRepository.findAll();
        assertThat(assetAcquisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAcquisitionMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetAcquisitionRepository.findAll().size();
        // set the field null
        assetAcquisition.setAcquisitionMonth(null);

        // Create the AssetAcquisition, which fails.
        AssetAcquisitionDTO assetAcquisitionDTO = assetAcquisitionMapper.toDto(assetAcquisition);

        restAssetAcquisitionMockMvc.perform(post("/api/asset-acquisitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetAcquisitionDTO)))
            .andExpect(status().isBadRequest());

        List<AssetAcquisition> assetAcquisitionList = assetAcquisitionRepository.findAll();
        assertThat(assetAcquisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAssetSerialIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetAcquisitionRepository.findAll().size();
        // set the field null
        assetAcquisition.setAssetSerial(null);

        // Create the AssetAcquisition, which fails.
        AssetAcquisitionDTO assetAcquisitionDTO = assetAcquisitionMapper.toDto(assetAcquisition);

        restAssetAcquisitionMockMvc.perform(post("/api/asset-acquisitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetAcquisitionDTO)))
            .andExpect(status().isBadRequest());

        List<AssetAcquisition> assetAcquisitionList = assetAcquisitionRepository.findAll();
        assertThat(assetAcquisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServiceOutletCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetAcquisitionRepository.findAll().size();
        // set the field null
        assetAcquisition.setServiceOutletCode(null);

        // Create the AssetAcquisition, which fails.
        AssetAcquisitionDTO assetAcquisitionDTO = assetAcquisitionMapper.toDto(assetAcquisition);

        restAssetAcquisitionMockMvc.perform(post("/api/asset-acquisitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetAcquisitionDTO)))
            .andExpect(status().isBadRequest());

        List<AssetAcquisition> assetAcquisitionList = assetAcquisitionRepository.findAll();
        assertThat(assetAcquisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAcquisitionTransactionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetAcquisitionRepository.findAll().size();
        // set the field null
        assetAcquisition.setAcquisitionTransactionId(null);

        // Create the AssetAcquisition, which fails.
        AssetAcquisitionDTO assetAcquisitionDTO = assetAcquisitionMapper.toDto(assetAcquisition);

        restAssetAcquisitionMockMvc.perform(post("/api/asset-acquisitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetAcquisitionDTO)))
            .andExpect(status().isBadRequest());

        List<AssetAcquisition> assetAcquisitionList = assetAcquisitionRepository.findAll();
        assertThat(assetAcquisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAssetCategoryIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetAcquisitionRepository.findAll().size();
        // set the field null
        assetAcquisition.setAssetCategoryId(null);

        // Create the AssetAcquisition, which fails.
        AssetAcquisitionDTO assetAcquisitionDTO = assetAcquisitionMapper.toDto(assetAcquisition);

        restAssetAcquisitionMockMvc.perform(post("/api/asset-acquisitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetAcquisitionDTO)))
            .andExpect(status().isBadRequest());

        List<AssetAcquisition> assetAcquisitionList = assetAcquisitionRepository.findAll();
        assertThat(assetAcquisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPurchaseAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetAcquisitionRepository.findAll().size();
        // set the field null
        assetAcquisition.setPurchaseAmount(null);

        // Create the AssetAcquisition, which fails.
        AssetAcquisitionDTO assetAcquisitionDTO = assetAcquisitionMapper.toDto(assetAcquisition);

        restAssetAcquisitionMockMvc.perform(post("/api/asset-acquisitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetAcquisitionDTO)))
            .andExpect(status().isBadRequest());

        List<AssetAcquisition> assetAcquisitionList = assetAcquisitionRepository.findAll();
        assertThat(assetAcquisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitions() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList
        restAssetAcquisitionMockMvc.perform(get("/api/asset-acquisitions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetAcquisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].acquisitionMonth").value(hasItem(DEFAULT_ACQUISITION_MONTH.toString())))
            .andExpect(jsonPath("$.[*].assetSerial").value(hasItem(DEFAULT_ASSET_SERIAL)))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].acquisitionTransactionId").value(hasItem(DEFAULT_ACQUISITION_TRANSACTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].assetCategoryId").value(hasItem(DEFAULT_ASSET_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].purchaseAmount").value(hasItem(DEFAULT_PURCHASE_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].assetDealerId").value(hasItem(DEFAULT_ASSET_DEALER_ID.intValue())))
            .andExpect(jsonPath("$.[*].assetInvoiceId").value(hasItem(DEFAULT_ASSET_INVOICE_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getAssetAcquisition() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get the assetAcquisition
        restAssetAcquisitionMockMvc.perform(get("/api/asset-acquisitions/{id}", assetAcquisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(assetAcquisition.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.acquisitionMonth").value(DEFAULT_ACQUISITION_MONTH.toString()))
            .andExpect(jsonPath("$.assetSerial").value(DEFAULT_ASSET_SERIAL))
            .andExpect(jsonPath("$.serviceOutletCode").value(DEFAULT_SERVICE_OUTLET_CODE))
            .andExpect(jsonPath("$.acquisitionTransactionId").value(DEFAULT_ACQUISITION_TRANSACTION_ID.intValue()))
            .andExpect(jsonPath("$.assetCategoryId").value(DEFAULT_ASSET_CATEGORY_ID.intValue()))
            .andExpect(jsonPath("$.purchaseAmount").value(DEFAULT_PURCHASE_AMOUNT.intValue()))
            .andExpect(jsonPath("$.assetDealerId").value(DEFAULT_ASSET_DEALER_ID.intValue()))
            .andExpect(jsonPath("$.assetInvoiceId").value(DEFAULT_ASSET_INVOICE_ID.intValue()));
    }


    @Test
    @Transactional
    public void getAssetAcquisitionsByIdFiltering() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        Long id = assetAcquisition.getId();

        defaultAssetAcquisitionShouldBeFound("id.equals=" + id);
        defaultAssetAcquisitionShouldNotBeFound("id.notEquals=" + id);

        defaultAssetAcquisitionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetAcquisitionShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetAcquisitionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetAcquisitionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAssetAcquisitionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where description equals to DEFAULT_DESCRIPTION
        defaultAssetAcquisitionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the assetAcquisitionList where description equals to UPDATED_DESCRIPTION
        defaultAssetAcquisitionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where description not equals to DEFAULT_DESCRIPTION
        defaultAssetAcquisitionShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the assetAcquisitionList where description not equals to UPDATED_DESCRIPTION
        defaultAssetAcquisitionShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAssetAcquisitionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the assetAcquisitionList where description equals to UPDATED_DESCRIPTION
        defaultAssetAcquisitionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where description is not null
        defaultAssetAcquisitionShouldBeFound("description.specified=true");

        // Get all the assetAcquisitionList where description is null
        defaultAssetAcquisitionShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllAssetAcquisitionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where description contains DEFAULT_DESCRIPTION
        defaultAssetAcquisitionShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the assetAcquisitionList where description contains UPDATED_DESCRIPTION
        defaultAssetAcquisitionShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where description does not contain DEFAULT_DESCRIPTION
        defaultAssetAcquisitionShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the assetAcquisitionList where description does not contain UPDATED_DESCRIPTION
        defaultAssetAcquisitionShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAcquisitionMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where acquisitionMonth equals to DEFAULT_ACQUISITION_MONTH
        defaultAssetAcquisitionShouldBeFound("acquisitionMonth.equals=" + DEFAULT_ACQUISITION_MONTH);

        // Get all the assetAcquisitionList where acquisitionMonth equals to UPDATED_ACQUISITION_MONTH
        defaultAssetAcquisitionShouldNotBeFound("acquisitionMonth.equals=" + UPDATED_ACQUISITION_MONTH);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAcquisitionMonthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where acquisitionMonth not equals to DEFAULT_ACQUISITION_MONTH
        defaultAssetAcquisitionShouldNotBeFound("acquisitionMonth.notEquals=" + DEFAULT_ACQUISITION_MONTH);

        // Get all the assetAcquisitionList where acquisitionMonth not equals to UPDATED_ACQUISITION_MONTH
        defaultAssetAcquisitionShouldBeFound("acquisitionMonth.notEquals=" + UPDATED_ACQUISITION_MONTH);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAcquisitionMonthIsInShouldWork() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where acquisitionMonth in DEFAULT_ACQUISITION_MONTH or UPDATED_ACQUISITION_MONTH
        defaultAssetAcquisitionShouldBeFound("acquisitionMonth.in=" + DEFAULT_ACQUISITION_MONTH + "," + UPDATED_ACQUISITION_MONTH);

        // Get all the assetAcquisitionList where acquisitionMonth equals to UPDATED_ACQUISITION_MONTH
        defaultAssetAcquisitionShouldNotBeFound("acquisitionMonth.in=" + UPDATED_ACQUISITION_MONTH);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAcquisitionMonthIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where acquisitionMonth is not null
        defaultAssetAcquisitionShouldBeFound("acquisitionMonth.specified=true");

        // Get all the assetAcquisitionList where acquisitionMonth is null
        defaultAssetAcquisitionShouldNotBeFound("acquisitionMonth.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAcquisitionMonthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where acquisitionMonth is greater than or equal to DEFAULT_ACQUISITION_MONTH
        defaultAssetAcquisitionShouldBeFound("acquisitionMonth.greaterThanOrEqual=" + DEFAULT_ACQUISITION_MONTH);

        // Get all the assetAcquisitionList where acquisitionMonth is greater than or equal to UPDATED_ACQUISITION_MONTH
        defaultAssetAcquisitionShouldNotBeFound("acquisitionMonth.greaterThanOrEqual=" + UPDATED_ACQUISITION_MONTH);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAcquisitionMonthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where acquisitionMonth is less than or equal to DEFAULT_ACQUISITION_MONTH
        defaultAssetAcquisitionShouldBeFound("acquisitionMonth.lessThanOrEqual=" + DEFAULT_ACQUISITION_MONTH);

        // Get all the assetAcquisitionList where acquisitionMonth is less than or equal to SMALLER_ACQUISITION_MONTH
        defaultAssetAcquisitionShouldNotBeFound("acquisitionMonth.lessThanOrEqual=" + SMALLER_ACQUISITION_MONTH);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAcquisitionMonthIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where acquisitionMonth is less than DEFAULT_ACQUISITION_MONTH
        defaultAssetAcquisitionShouldNotBeFound("acquisitionMonth.lessThan=" + DEFAULT_ACQUISITION_MONTH);

        // Get all the assetAcquisitionList where acquisitionMonth is less than UPDATED_ACQUISITION_MONTH
        defaultAssetAcquisitionShouldBeFound("acquisitionMonth.lessThan=" + UPDATED_ACQUISITION_MONTH);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAcquisitionMonthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where acquisitionMonth is greater than DEFAULT_ACQUISITION_MONTH
        defaultAssetAcquisitionShouldNotBeFound("acquisitionMonth.greaterThan=" + DEFAULT_ACQUISITION_MONTH);

        // Get all the assetAcquisitionList where acquisitionMonth is greater than SMALLER_ACQUISITION_MONTH
        defaultAssetAcquisitionShouldBeFound("acquisitionMonth.greaterThan=" + SMALLER_ACQUISITION_MONTH);
    }


    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetSerialIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetSerial equals to DEFAULT_ASSET_SERIAL
        defaultAssetAcquisitionShouldBeFound("assetSerial.equals=" + DEFAULT_ASSET_SERIAL);

        // Get all the assetAcquisitionList where assetSerial equals to UPDATED_ASSET_SERIAL
        defaultAssetAcquisitionShouldNotBeFound("assetSerial.equals=" + UPDATED_ASSET_SERIAL);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetSerialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetSerial not equals to DEFAULT_ASSET_SERIAL
        defaultAssetAcquisitionShouldNotBeFound("assetSerial.notEquals=" + DEFAULT_ASSET_SERIAL);

        // Get all the assetAcquisitionList where assetSerial not equals to UPDATED_ASSET_SERIAL
        defaultAssetAcquisitionShouldBeFound("assetSerial.notEquals=" + UPDATED_ASSET_SERIAL);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetSerialIsInShouldWork() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetSerial in DEFAULT_ASSET_SERIAL or UPDATED_ASSET_SERIAL
        defaultAssetAcquisitionShouldBeFound("assetSerial.in=" + DEFAULT_ASSET_SERIAL + "," + UPDATED_ASSET_SERIAL);

        // Get all the assetAcquisitionList where assetSerial equals to UPDATED_ASSET_SERIAL
        defaultAssetAcquisitionShouldNotBeFound("assetSerial.in=" + UPDATED_ASSET_SERIAL);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetSerialIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetSerial is not null
        defaultAssetAcquisitionShouldBeFound("assetSerial.specified=true");

        // Get all the assetAcquisitionList where assetSerial is null
        defaultAssetAcquisitionShouldNotBeFound("assetSerial.specified=false");
    }
                @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetSerialContainsSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetSerial contains DEFAULT_ASSET_SERIAL
        defaultAssetAcquisitionShouldBeFound("assetSerial.contains=" + DEFAULT_ASSET_SERIAL);

        // Get all the assetAcquisitionList where assetSerial contains UPDATED_ASSET_SERIAL
        defaultAssetAcquisitionShouldNotBeFound("assetSerial.contains=" + UPDATED_ASSET_SERIAL);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetSerialNotContainsSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetSerial does not contain DEFAULT_ASSET_SERIAL
        defaultAssetAcquisitionShouldNotBeFound("assetSerial.doesNotContain=" + DEFAULT_ASSET_SERIAL);

        // Get all the assetAcquisitionList where assetSerial does not contain UPDATED_ASSET_SERIAL
        defaultAssetAcquisitionShouldBeFound("assetSerial.doesNotContain=" + UPDATED_ASSET_SERIAL);
    }


    @Test
    @Transactional
    public void getAllAssetAcquisitionsByServiceOutletCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where serviceOutletCode equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultAssetAcquisitionShouldBeFound("serviceOutletCode.equals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the assetAcquisitionList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultAssetAcquisitionShouldNotBeFound("serviceOutletCode.equals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByServiceOutletCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where serviceOutletCode not equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultAssetAcquisitionShouldNotBeFound("serviceOutletCode.notEquals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the assetAcquisitionList where serviceOutletCode not equals to UPDATED_SERVICE_OUTLET_CODE
        defaultAssetAcquisitionShouldBeFound("serviceOutletCode.notEquals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByServiceOutletCodeIsInShouldWork() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where serviceOutletCode in DEFAULT_SERVICE_OUTLET_CODE or UPDATED_SERVICE_OUTLET_CODE
        defaultAssetAcquisitionShouldBeFound("serviceOutletCode.in=" + DEFAULT_SERVICE_OUTLET_CODE + "," + UPDATED_SERVICE_OUTLET_CODE);

        // Get all the assetAcquisitionList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultAssetAcquisitionShouldNotBeFound("serviceOutletCode.in=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByServiceOutletCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where serviceOutletCode is not null
        defaultAssetAcquisitionShouldBeFound("serviceOutletCode.specified=true");

        // Get all the assetAcquisitionList where serviceOutletCode is null
        defaultAssetAcquisitionShouldNotBeFound("serviceOutletCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllAssetAcquisitionsByServiceOutletCodeContainsSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where serviceOutletCode contains DEFAULT_SERVICE_OUTLET_CODE
        defaultAssetAcquisitionShouldBeFound("serviceOutletCode.contains=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the assetAcquisitionList where serviceOutletCode contains UPDATED_SERVICE_OUTLET_CODE
        defaultAssetAcquisitionShouldNotBeFound("serviceOutletCode.contains=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByServiceOutletCodeNotContainsSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where serviceOutletCode does not contain DEFAULT_SERVICE_OUTLET_CODE
        defaultAssetAcquisitionShouldNotBeFound("serviceOutletCode.doesNotContain=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the assetAcquisitionList where serviceOutletCode does not contain UPDATED_SERVICE_OUTLET_CODE
        defaultAssetAcquisitionShouldBeFound("serviceOutletCode.doesNotContain=" + UPDATED_SERVICE_OUTLET_CODE);
    }


    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAcquisitionTransactionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where acquisitionTransactionId equals to DEFAULT_ACQUISITION_TRANSACTION_ID
        defaultAssetAcquisitionShouldBeFound("acquisitionTransactionId.equals=" + DEFAULT_ACQUISITION_TRANSACTION_ID);

        // Get all the assetAcquisitionList where acquisitionTransactionId equals to UPDATED_ACQUISITION_TRANSACTION_ID
        defaultAssetAcquisitionShouldNotBeFound("acquisitionTransactionId.equals=" + UPDATED_ACQUISITION_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAcquisitionTransactionIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where acquisitionTransactionId not equals to DEFAULT_ACQUISITION_TRANSACTION_ID
        defaultAssetAcquisitionShouldNotBeFound("acquisitionTransactionId.notEquals=" + DEFAULT_ACQUISITION_TRANSACTION_ID);

        // Get all the assetAcquisitionList where acquisitionTransactionId not equals to UPDATED_ACQUISITION_TRANSACTION_ID
        defaultAssetAcquisitionShouldBeFound("acquisitionTransactionId.notEquals=" + UPDATED_ACQUISITION_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAcquisitionTransactionIdIsInShouldWork() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where acquisitionTransactionId in DEFAULT_ACQUISITION_TRANSACTION_ID or UPDATED_ACQUISITION_TRANSACTION_ID
        defaultAssetAcquisitionShouldBeFound("acquisitionTransactionId.in=" + DEFAULT_ACQUISITION_TRANSACTION_ID + "," + UPDATED_ACQUISITION_TRANSACTION_ID);

        // Get all the assetAcquisitionList where acquisitionTransactionId equals to UPDATED_ACQUISITION_TRANSACTION_ID
        defaultAssetAcquisitionShouldNotBeFound("acquisitionTransactionId.in=" + UPDATED_ACQUISITION_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAcquisitionTransactionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where acquisitionTransactionId is not null
        defaultAssetAcquisitionShouldBeFound("acquisitionTransactionId.specified=true");

        // Get all the assetAcquisitionList where acquisitionTransactionId is null
        defaultAssetAcquisitionShouldNotBeFound("acquisitionTransactionId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAcquisitionTransactionIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where acquisitionTransactionId is greater than or equal to DEFAULT_ACQUISITION_TRANSACTION_ID
        defaultAssetAcquisitionShouldBeFound("acquisitionTransactionId.greaterThanOrEqual=" + DEFAULT_ACQUISITION_TRANSACTION_ID);

        // Get all the assetAcquisitionList where acquisitionTransactionId is greater than or equal to UPDATED_ACQUISITION_TRANSACTION_ID
        defaultAssetAcquisitionShouldNotBeFound("acquisitionTransactionId.greaterThanOrEqual=" + UPDATED_ACQUISITION_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAcquisitionTransactionIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where acquisitionTransactionId is less than or equal to DEFAULT_ACQUISITION_TRANSACTION_ID
        defaultAssetAcquisitionShouldBeFound("acquisitionTransactionId.lessThanOrEqual=" + DEFAULT_ACQUISITION_TRANSACTION_ID);

        // Get all the assetAcquisitionList where acquisitionTransactionId is less than or equal to SMALLER_ACQUISITION_TRANSACTION_ID
        defaultAssetAcquisitionShouldNotBeFound("acquisitionTransactionId.lessThanOrEqual=" + SMALLER_ACQUISITION_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAcquisitionTransactionIdIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where acquisitionTransactionId is less than DEFAULT_ACQUISITION_TRANSACTION_ID
        defaultAssetAcquisitionShouldNotBeFound("acquisitionTransactionId.lessThan=" + DEFAULT_ACQUISITION_TRANSACTION_ID);

        // Get all the assetAcquisitionList where acquisitionTransactionId is less than UPDATED_ACQUISITION_TRANSACTION_ID
        defaultAssetAcquisitionShouldBeFound("acquisitionTransactionId.lessThan=" + UPDATED_ACQUISITION_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAcquisitionTransactionIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where acquisitionTransactionId is greater than DEFAULT_ACQUISITION_TRANSACTION_ID
        defaultAssetAcquisitionShouldNotBeFound("acquisitionTransactionId.greaterThan=" + DEFAULT_ACQUISITION_TRANSACTION_ID);

        // Get all the assetAcquisitionList where acquisitionTransactionId is greater than SMALLER_ACQUISITION_TRANSACTION_ID
        defaultAssetAcquisitionShouldBeFound("acquisitionTransactionId.greaterThan=" + SMALLER_ACQUISITION_TRANSACTION_ID);
    }


    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetCategoryIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetCategoryId equals to DEFAULT_ASSET_CATEGORY_ID
        defaultAssetAcquisitionShouldBeFound("assetCategoryId.equals=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the assetAcquisitionList where assetCategoryId equals to UPDATED_ASSET_CATEGORY_ID
        defaultAssetAcquisitionShouldNotBeFound("assetCategoryId.equals=" + UPDATED_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetCategoryIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetCategoryId not equals to DEFAULT_ASSET_CATEGORY_ID
        defaultAssetAcquisitionShouldNotBeFound("assetCategoryId.notEquals=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the assetAcquisitionList where assetCategoryId not equals to UPDATED_ASSET_CATEGORY_ID
        defaultAssetAcquisitionShouldBeFound("assetCategoryId.notEquals=" + UPDATED_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetCategoryIdIsInShouldWork() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetCategoryId in DEFAULT_ASSET_CATEGORY_ID or UPDATED_ASSET_CATEGORY_ID
        defaultAssetAcquisitionShouldBeFound("assetCategoryId.in=" + DEFAULT_ASSET_CATEGORY_ID + "," + UPDATED_ASSET_CATEGORY_ID);

        // Get all the assetAcquisitionList where assetCategoryId equals to UPDATED_ASSET_CATEGORY_ID
        defaultAssetAcquisitionShouldNotBeFound("assetCategoryId.in=" + UPDATED_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetCategoryIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetCategoryId is not null
        defaultAssetAcquisitionShouldBeFound("assetCategoryId.specified=true");

        // Get all the assetAcquisitionList where assetCategoryId is null
        defaultAssetAcquisitionShouldNotBeFound("assetCategoryId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetCategoryIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetCategoryId is greater than or equal to DEFAULT_ASSET_CATEGORY_ID
        defaultAssetAcquisitionShouldBeFound("assetCategoryId.greaterThanOrEqual=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the assetAcquisitionList where assetCategoryId is greater than or equal to UPDATED_ASSET_CATEGORY_ID
        defaultAssetAcquisitionShouldNotBeFound("assetCategoryId.greaterThanOrEqual=" + UPDATED_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetCategoryIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetCategoryId is less than or equal to DEFAULT_ASSET_CATEGORY_ID
        defaultAssetAcquisitionShouldBeFound("assetCategoryId.lessThanOrEqual=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the assetAcquisitionList where assetCategoryId is less than or equal to SMALLER_ASSET_CATEGORY_ID
        defaultAssetAcquisitionShouldNotBeFound("assetCategoryId.lessThanOrEqual=" + SMALLER_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetCategoryIdIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetCategoryId is less than DEFAULT_ASSET_CATEGORY_ID
        defaultAssetAcquisitionShouldNotBeFound("assetCategoryId.lessThan=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the assetAcquisitionList where assetCategoryId is less than UPDATED_ASSET_CATEGORY_ID
        defaultAssetAcquisitionShouldBeFound("assetCategoryId.lessThan=" + UPDATED_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetCategoryIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetCategoryId is greater than DEFAULT_ASSET_CATEGORY_ID
        defaultAssetAcquisitionShouldNotBeFound("assetCategoryId.greaterThan=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the assetAcquisitionList where assetCategoryId is greater than SMALLER_ASSET_CATEGORY_ID
        defaultAssetAcquisitionShouldBeFound("assetCategoryId.greaterThan=" + SMALLER_ASSET_CATEGORY_ID);
    }


    @Test
    @Transactional
    public void getAllAssetAcquisitionsByPurchaseAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where purchaseAmount equals to DEFAULT_PURCHASE_AMOUNT
        defaultAssetAcquisitionShouldBeFound("purchaseAmount.equals=" + DEFAULT_PURCHASE_AMOUNT);

        // Get all the assetAcquisitionList where purchaseAmount equals to UPDATED_PURCHASE_AMOUNT
        defaultAssetAcquisitionShouldNotBeFound("purchaseAmount.equals=" + UPDATED_PURCHASE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByPurchaseAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where purchaseAmount not equals to DEFAULT_PURCHASE_AMOUNT
        defaultAssetAcquisitionShouldNotBeFound("purchaseAmount.notEquals=" + DEFAULT_PURCHASE_AMOUNT);

        // Get all the assetAcquisitionList where purchaseAmount not equals to UPDATED_PURCHASE_AMOUNT
        defaultAssetAcquisitionShouldBeFound("purchaseAmount.notEquals=" + UPDATED_PURCHASE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByPurchaseAmountIsInShouldWork() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where purchaseAmount in DEFAULT_PURCHASE_AMOUNT or UPDATED_PURCHASE_AMOUNT
        defaultAssetAcquisitionShouldBeFound("purchaseAmount.in=" + DEFAULT_PURCHASE_AMOUNT + "," + UPDATED_PURCHASE_AMOUNT);

        // Get all the assetAcquisitionList where purchaseAmount equals to UPDATED_PURCHASE_AMOUNT
        defaultAssetAcquisitionShouldNotBeFound("purchaseAmount.in=" + UPDATED_PURCHASE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByPurchaseAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where purchaseAmount is not null
        defaultAssetAcquisitionShouldBeFound("purchaseAmount.specified=true");

        // Get all the assetAcquisitionList where purchaseAmount is null
        defaultAssetAcquisitionShouldNotBeFound("purchaseAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByPurchaseAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where purchaseAmount is greater than or equal to DEFAULT_PURCHASE_AMOUNT
        defaultAssetAcquisitionShouldBeFound("purchaseAmount.greaterThanOrEqual=" + DEFAULT_PURCHASE_AMOUNT);

        // Get all the assetAcquisitionList where purchaseAmount is greater than or equal to UPDATED_PURCHASE_AMOUNT
        defaultAssetAcquisitionShouldNotBeFound("purchaseAmount.greaterThanOrEqual=" + UPDATED_PURCHASE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByPurchaseAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where purchaseAmount is less than or equal to DEFAULT_PURCHASE_AMOUNT
        defaultAssetAcquisitionShouldBeFound("purchaseAmount.lessThanOrEqual=" + DEFAULT_PURCHASE_AMOUNT);

        // Get all the assetAcquisitionList where purchaseAmount is less than or equal to SMALLER_PURCHASE_AMOUNT
        defaultAssetAcquisitionShouldNotBeFound("purchaseAmount.lessThanOrEqual=" + SMALLER_PURCHASE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByPurchaseAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where purchaseAmount is less than DEFAULT_PURCHASE_AMOUNT
        defaultAssetAcquisitionShouldNotBeFound("purchaseAmount.lessThan=" + DEFAULT_PURCHASE_AMOUNT);

        // Get all the assetAcquisitionList where purchaseAmount is less than UPDATED_PURCHASE_AMOUNT
        defaultAssetAcquisitionShouldBeFound("purchaseAmount.lessThan=" + UPDATED_PURCHASE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByPurchaseAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where purchaseAmount is greater than DEFAULT_PURCHASE_AMOUNT
        defaultAssetAcquisitionShouldNotBeFound("purchaseAmount.greaterThan=" + DEFAULT_PURCHASE_AMOUNT);

        // Get all the assetAcquisitionList where purchaseAmount is greater than SMALLER_PURCHASE_AMOUNT
        defaultAssetAcquisitionShouldBeFound("purchaseAmount.greaterThan=" + SMALLER_PURCHASE_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetDealerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetDealerId equals to DEFAULT_ASSET_DEALER_ID
        defaultAssetAcquisitionShouldBeFound("assetDealerId.equals=" + DEFAULT_ASSET_DEALER_ID);

        // Get all the assetAcquisitionList where assetDealerId equals to UPDATED_ASSET_DEALER_ID
        defaultAssetAcquisitionShouldNotBeFound("assetDealerId.equals=" + UPDATED_ASSET_DEALER_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetDealerIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetDealerId not equals to DEFAULT_ASSET_DEALER_ID
        defaultAssetAcquisitionShouldNotBeFound("assetDealerId.notEquals=" + DEFAULT_ASSET_DEALER_ID);

        // Get all the assetAcquisitionList where assetDealerId not equals to UPDATED_ASSET_DEALER_ID
        defaultAssetAcquisitionShouldBeFound("assetDealerId.notEquals=" + UPDATED_ASSET_DEALER_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetDealerIdIsInShouldWork() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetDealerId in DEFAULT_ASSET_DEALER_ID or UPDATED_ASSET_DEALER_ID
        defaultAssetAcquisitionShouldBeFound("assetDealerId.in=" + DEFAULT_ASSET_DEALER_ID + "," + UPDATED_ASSET_DEALER_ID);

        // Get all the assetAcquisitionList where assetDealerId equals to UPDATED_ASSET_DEALER_ID
        defaultAssetAcquisitionShouldNotBeFound("assetDealerId.in=" + UPDATED_ASSET_DEALER_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetDealerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetDealerId is not null
        defaultAssetAcquisitionShouldBeFound("assetDealerId.specified=true");

        // Get all the assetAcquisitionList where assetDealerId is null
        defaultAssetAcquisitionShouldNotBeFound("assetDealerId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetDealerIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetDealerId is greater than or equal to DEFAULT_ASSET_DEALER_ID
        defaultAssetAcquisitionShouldBeFound("assetDealerId.greaterThanOrEqual=" + DEFAULT_ASSET_DEALER_ID);

        // Get all the assetAcquisitionList where assetDealerId is greater than or equal to UPDATED_ASSET_DEALER_ID
        defaultAssetAcquisitionShouldNotBeFound("assetDealerId.greaterThanOrEqual=" + UPDATED_ASSET_DEALER_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetDealerIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetDealerId is less than or equal to DEFAULT_ASSET_DEALER_ID
        defaultAssetAcquisitionShouldBeFound("assetDealerId.lessThanOrEqual=" + DEFAULT_ASSET_DEALER_ID);

        // Get all the assetAcquisitionList where assetDealerId is less than or equal to SMALLER_ASSET_DEALER_ID
        defaultAssetAcquisitionShouldNotBeFound("assetDealerId.lessThanOrEqual=" + SMALLER_ASSET_DEALER_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetDealerIdIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetDealerId is less than DEFAULT_ASSET_DEALER_ID
        defaultAssetAcquisitionShouldNotBeFound("assetDealerId.lessThan=" + DEFAULT_ASSET_DEALER_ID);

        // Get all the assetAcquisitionList where assetDealerId is less than UPDATED_ASSET_DEALER_ID
        defaultAssetAcquisitionShouldBeFound("assetDealerId.lessThan=" + UPDATED_ASSET_DEALER_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetDealerIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetDealerId is greater than DEFAULT_ASSET_DEALER_ID
        defaultAssetAcquisitionShouldNotBeFound("assetDealerId.greaterThan=" + DEFAULT_ASSET_DEALER_ID);

        // Get all the assetAcquisitionList where assetDealerId is greater than SMALLER_ASSET_DEALER_ID
        defaultAssetAcquisitionShouldBeFound("assetDealerId.greaterThan=" + SMALLER_ASSET_DEALER_ID);
    }


    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetInvoiceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetInvoiceId equals to DEFAULT_ASSET_INVOICE_ID
        defaultAssetAcquisitionShouldBeFound("assetInvoiceId.equals=" + DEFAULT_ASSET_INVOICE_ID);

        // Get all the assetAcquisitionList where assetInvoiceId equals to UPDATED_ASSET_INVOICE_ID
        defaultAssetAcquisitionShouldNotBeFound("assetInvoiceId.equals=" + UPDATED_ASSET_INVOICE_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetInvoiceIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetInvoiceId not equals to DEFAULT_ASSET_INVOICE_ID
        defaultAssetAcquisitionShouldNotBeFound("assetInvoiceId.notEquals=" + DEFAULT_ASSET_INVOICE_ID);

        // Get all the assetAcquisitionList where assetInvoiceId not equals to UPDATED_ASSET_INVOICE_ID
        defaultAssetAcquisitionShouldBeFound("assetInvoiceId.notEquals=" + UPDATED_ASSET_INVOICE_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetInvoiceIdIsInShouldWork() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetInvoiceId in DEFAULT_ASSET_INVOICE_ID or UPDATED_ASSET_INVOICE_ID
        defaultAssetAcquisitionShouldBeFound("assetInvoiceId.in=" + DEFAULT_ASSET_INVOICE_ID + "," + UPDATED_ASSET_INVOICE_ID);

        // Get all the assetAcquisitionList where assetInvoiceId equals to UPDATED_ASSET_INVOICE_ID
        defaultAssetAcquisitionShouldNotBeFound("assetInvoiceId.in=" + UPDATED_ASSET_INVOICE_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetInvoiceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetInvoiceId is not null
        defaultAssetAcquisitionShouldBeFound("assetInvoiceId.specified=true");

        // Get all the assetAcquisitionList where assetInvoiceId is null
        defaultAssetAcquisitionShouldNotBeFound("assetInvoiceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetInvoiceIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetInvoiceId is greater than or equal to DEFAULT_ASSET_INVOICE_ID
        defaultAssetAcquisitionShouldBeFound("assetInvoiceId.greaterThanOrEqual=" + DEFAULT_ASSET_INVOICE_ID);

        // Get all the assetAcquisitionList where assetInvoiceId is greater than or equal to UPDATED_ASSET_INVOICE_ID
        defaultAssetAcquisitionShouldNotBeFound("assetInvoiceId.greaterThanOrEqual=" + UPDATED_ASSET_INVOICE_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetInvoiceIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetInvoiceId is less than or equal to DEFAULT_ASSET_INVOICE_ID
        defaultAssetAcquisitionShouldBeFound("assetInvoiceId.lessThanOrEqual=" + DEFAULT_ASSET_INVOICE_ID);

        // Get all the assetAcquisitionList where assetInvoiceId is less than or equal to SMALLER_ASSET_INVOICE_ID
        defaultAssetAcquisitionShouldNotBeFound("assetInvoiceId.lessThanOrEqual=" + SMALLER_ASSET_INVOICE_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetInvoiceIdIsLessThanSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetInvoiceId is less than DEFAULT_ASSET_INVOICE_ID
        defaultAssetAcquisitionShouldNotBeFound("assetInvoiceId.lessThan=" + DEFAULT_ASSET_INVOICE_ID);

        // Get all the assetAcquisitionList where assetInvoiceId is less than UPDATED_ASSET_INVOICE_ID
        defaultAssetAcquisitionShouldBeFound("assetInvoiceId.lessThan=" + UPDATED_ASSET_INVOICE_ID);
    }

    @Test
    @Transactional
    public void getAllAssetAcquisitionsByAssetInvoiceIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        // Get all the assetAcquisitionList where assetInvoiceId is greater than DEFAULT_ASSET_INVOICE_ID
        defaultAssetAcquisitionShouldNotBeFound("assetInvoiceId.greaterThan=" + DEFAULT_ASSET_INVOICE_ID);

        // Get all the assetAcquisitionList where assetInvoiceId is greater than SMALLER_ASSET_INVOICE_ID
        defaultAssetAcquisitionShouldBeFound("assetInvoiceId.greaterThan=" + SMALLER_ASSET_INVOICE_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetAcquisitionShouldBeFound(String filter) throws Exception {
        restAssetAcquisitionMockMvc.perform(get("/api/asset-acquisitions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetAcquisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].acquisitionMonth").value(hasItem(DEFAULT_ACQUISITION_MONTH.toString())))
            .andExpect(jsonPath("$.[*].assetSerial").value(hasItem(DEFAULT_ASSET_SERIAL)))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].acquisitionTransactionId").value(hasItem(DEFAULT_ACQUISITION_TRANSACTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].assetCategoryId").value(hasItem(DEFAULT_ASSET_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].purchaseAmount").value(hasItem(DEFAULT_PURCHASE_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].assetDealerId").value(hasItem(DEFAULT_ASSET_DEALER_ID.intValue())))
            .andExpect(jsonPath("$.[*].assetInvoiceId").value(hasItem(DEFAULT_ASSET_INVOICE_ID.intValue())));

        // Check, that the count call also returns 1
        restAssetAcquisitionMockMvc.perform(get("/api/asset-acquisitions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetAcquisitionShouldNotBeFound(String filter) throws Exception {
        restAssetAcquisitionMockMvc.perform(get("/api/asset-acquisitions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetAcquisitionMockMvc.perform(get("/api/asset-acquisitions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAssetAcquisition() throws Exception {
        // Get the assetAcquisition
        restAssetAcquisitionMockMvc.perform(get("/api/asset-acquisitions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssetAcquisition() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        int databaseSizeBeforeUpdate = assetAcquisitionRepository.findAll().size();

        // Update the assetAcquisition
        AssetAcquisition updatedAssetAcquisition = assetAcquisitionRepository.findById(assetAcquisition.getId()).get();
        // Disconnect from session so that the updates on updatedAssetAcquisition are not directly saved in db
        em.detach(updatedAssetAcquisition);
        updatedAssetAcquisition
            .description(UPDATED_DESCRIPTION)
            .acquisitionMonth(UPDATED_ACQUISITION_MONTH)
            .assetSerial(UPDATED_ASSET_SERIAL)
            .serviceOutletCode(UPDATED_SERVICE_OUTLET_CODE)
            .acquisitionTransactionId(UPDATED_ACQUISITION_TRANSACTION_ID)
            .assetCategoryId(UPDATED_ASSET_CATEGORY_ID)
            .purchaseAmount(UPDATED_PURCHASE_AMOUNT)
            .assetDealerId(UPDATED_ASSET_DEALER_ID)
            .assetInvoiceId(UPDATED_ASSET_INVOICE_ID);
        AssetAcquisitionDTO assetAcquisitionDTO = assetAcquisitionMapper.toDto(updatedAssetAcquisition);

        restAssetAcquisitionMockMvc.perform(put("/api/asset-acquisitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetAcquisitionDTO)))
            .andExpect(status().isOk());

        // Validate the AssetAcquisition in the database
        List<AssetAcquisition> assetAcquisitionList = assetAcquisitionRepository.findAll();
        assertThat(assetAcquisitionList).hasSize(databaseSizeBeforeUpdate);
        AssetAcquisition testAssetAcquisition = assetAcquisitionList.get(assetAcquisitionList.size() - 1);
        assertThat(testAssetAcquisition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssetAcquisition.getAcquisitionMonth()).isEqualTo(UPDATED_ACQUISITION_MONTH);
        assertThat(testAssetAcquisition.getAssetSerial()).isEqualTo(UPDATED_ASSET_SERIAL);
        assertThat(testAssetAcquisition.getServiceOutletCode()).isEqualTo(UPDATED_SERVICE_OUTLET_CODE);
        assertThat(testAssetAcquisition.getAcquisitionTransactionId()).isEqualTo(UPDATED_ACQUISITION_TRANSACTION_ID);
        assertThat(testAssetAcquisition.getAssetCategoryId()).isEqualTo(UPDATED_ASSET_CATEGORY_ID);
        assertThat(testAssetAcquisition.getPurchaseAmount()).isEqualTo(UPDATED_PURCHASE_AMOUNT);
        assertThat(testAssetAcquisition.getAssetDealerId()).isEqualTo(UPDATED_ASSET_DEALER_ID);
        assertThat(testAssetAcquisition.getAssetInvoiceId()).isEqualTo(UPDATED_ASSET_INVOICE_ID);

        // Validate the AssetAcquisition in Elasticsearch
        verify(mockAssetAcquisitionSearchRepository, times(1)).save(testAssetAcquisition);
    }

    @Test
    @Transactional
    public void updateNonExistingAssetAcquisition() throws Exception {
        int databaseSizeBeforeUpdate = assetAcquisitionRepository.findAll().size();

        // Create the AssetAcquisition
        AssetAcquisitionDTO assetAcquisitionDTO = assetAcquisitionMapper.toDto(assetAcquisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetAcquisitionMockMvc.perform(put("/api/asset-acquisitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetAcquisitionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AssetAcquisition in the database
        List<AssetAcquisition> assetAcquisitionList = assetAcquisitionRepository.findAll();
        assertThat(assetAcquisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetAcquisition in Elasticsearch
        verify(mockAssetAcquisitionSearchRepository, times(0)).save(assetAcquisition);
    }

    @Test
    @Transactional
    public void deleteAssetAcquisition() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);

        int databaseSizeBeforeDelete = assetAcquisitionRepository.findAll().size();

        // Delete the assetAcquisition
        restAssetAcquisitionMockMvc.perform(delete("/api/asset-acquisitions/{id}", assetAcquisition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetAcquisition> assetAcquisitionList = assetAcquisitionRepository.findAll();
        assertThat(assetAcquisitionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AssetAcquisition in Elasticsearch
        verify(mockAssetAcquisitionSearchRepository, times(1)).deleteById(assetAcquisition.getId());
    }

    @Test
    @Transactional
    public void searchAssetAcquisition() throws Exception {
        // Initialize the database
        assetAcquisitionRepository.saveAndFlush(assetAcquisition);
        when(mockAssetAcquisitionSearchRepository.search(queryStringQuery("id:" + assetAcquisition.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(assetAcquisition), PageRequest.of(0, 1), 1));
        // Search the assetAcquisition
        restAssetAcquisitionMockMvc.perform(get("/api/_search/asset-acquisitions?query=id:" + assetAcquisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetAcquisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].acquisitionMonth").value(hasItem(DEFAULT_ACQUISITION_MONTH.toString())))
            .andExpect(jsonPath("$.[*].assetSerial").value(hasItem(DEFAULT_ASSET_SERIAL)))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].acquisitionTransactionId").value(hasItem(DEFAULT_ACQUISITION_TRANSACTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].assetCategoryId").value(hasItem(DEFAULT_ASSET_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].purchaseAmount").value(hasItem(DEFAULT_PURCHASE_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].assetDealerId").value(hasItem(DEFAULT_ASSET_DEALER_ID.intValue())))
            .andExpect(jsonPath("$.[*].assetInvoiceId").value(hasItem(DEFAULT_ASSET_INVOICE_ID.intValue())));
    }
}
