package io.github.assets.web.rest;

import io.github.assets.FixedAssetServiceApp;
import io.github.assets.config.SecurityBeanOverrideConfiguration;
import io.github.assets.domain.AssetTransaction;
import io.github.assets.repository.AssetTransactionRepository;
import io.github.assets.repository.search.AssetTransactionSearchRepository;
import io.github.assets.service.AssetTransactionService;
import io.github.assets.service.dto.AssetTransactionDTO;
import io.github.assets.service.mapper.AssetTransactionMapper;
import io.github.assets.web.rest.errors.ExceptionTranslator;
import io.github.assets.service.dto.AssetTransactionCriteria;
import io.github.assets.service.AssetTransactionQueryService;

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
 * Integration tests for the {@link AssetTransactionResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, FixedAssetServiceApp.class})
public class AssetTransactionResourceIT {

    private static final String DEFAULT_TRANSACTION_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_REFERENCE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRANSACTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final Long DEFAULT_SCANNED_DOCUMENT_ID = 1L;
    private static final Long UPDATED_SCANNED_DOCUMENT_ID = 2L;
    private static final Long SMALLER_SCANNED_DOCUMENT_ID = 1L - 1L;

    private static final Long DEFAULT_TRANSACTION_APPROVAL_ID = 1L;
    private static final Long UPDATED_TRANSACTION_APPROVAL_ID = 2L;
    private static final Long SMALLER_TRANSACTION_APPROVAL_ID = 1L - 1L;

    @Autowired
    private AssetTransactionRepository assetTransactionRepository;

    @Autowired
    private AssetTransactionMapper assetTransactionMapper;

    @Autowired
    private AssetTransactionService assetTransactionService;

    /**
     * This repository is mocked in the io.github.assets.repository.search test package.
     *
     * @see io.github.assets.repository.search.AssetTransactionSearchRepositoryMockConfiguration
     */
    @Autowired
    private AssetTransactionSearchRepository mockAssetTransactionSearchRepository;

    @Autowired
    private AssetTransactionQueryService assetTransactionQueryService;

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

    private MockMvc restAssetTransactionMockMvc;

    private AssetTransaction assetTransaction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AssetTransactionResource assetTransactionResource = new AssetTransactionResource(assetTransactionService, assetTransactionQueryService);
        this.restAssetTransactionMockMvc = MockMvcBuilders.standaloneSetup(assetTransactionResource)
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
    public static AssetTransaction createEntity(EntityManager em) {
        AssetTransaction assetTransaction = new AssetTransaction()
            .transactionReference(DEFAULT_TRANSACTION_REFERENCE)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .scannedDocumentId(DEFAULT_SCANNED_DOCUMENT_ID)
            .transactionApprovalId(DEFAULT_TRANSACTION_APPROVAL_ID);
        return assetTransaction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetTransaction createUpdatedEntity(EntityManager em) {
        AssetTransaction assetTransaction = new AssetTransaction()
            .transactionReference(UPDATED_TRANSACTION_REFERENCE)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .scannedDocumentId(UPDATED_SCANNED_DOCUMENT_ID)
            .transactionApprovalId(UPDATED_TRANSACTION_APPROVAL_ID);
        return assetTransaction;
    }

    @BeforeEach
    public void initTest() {
        assetTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssetTransaction() throws Exception {
        int databaseSizeBeforeCreate = assetTransactionRepository.findAll().size();

        // Create the AssetTransaction
        AssetTransactionDTO assetTransactionDTO = assetTransactionMapper.toDto(assetTransaction);
        restAssetTransactionMockMvc.perform(post("/api/asset-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetTransactionDTO)))
            .andExpect(status().isCreated());

        // Validate the AssetTransaction in the database
        List<AssetTransaction> assetTransactionList = assetTransactionRepository.findAll();
        assertThat(assetTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        AssetTransaction testAssetTransaction = assetTransactionList.get(assetTransactionList.size() - 1);
        assertThat(testAssetTransaction.getTransactionReference()).isEqualTo(DEFAULT_TRANSACTION_REFERENCE);
        assertThat(testAssetTransaction.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testAssetTransaction.getScannedDocumentId()).isEqualTo(DEFAULT_SCANNED_DOCUMENT_ID);
        assertThat(testAssetTransaction.getTransactionApprovalId()).isEqualTo(DEFAULT_TRANSACTION_APPROVAL_ID);

        // Validate the AssetTransaction in Elasticsearch
        verify(mockAssetTransactionSearchRepository, times(1)).save(testAssetTransaction);
    }

    @Test
    @Transactional
    public void createAssetTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = assetTransactionRepository.findAll().size();

        // Create the AssetTransaction with an existing ID
        assetTransaction.setId(1L);
        AssetTransactionDTO assetTransactionDTO = assetTransactionMapper.toDto(assetTransaction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetTransactionMockMvc.perform(post("/api/asset-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AssetTransaction in the database
        List<AssetTransaction> assetTransactionList = assetTransactionRepository.findAll();
        assertThat(assetTransactionList).hasSize(databaseSizeBeforeCreate);

        // Validate the AssetTransaction in Elasticsearch
        verify(mockAssetTransactionSearchRepository, times(0)).save(assetTransaction);
    }


    @Test
    @Transactional
    public void checkTransactionReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetTransactionRepository.findAll().size();
        // set the field null
        assetTransaction.setTransactionReference(null);

        // Create the AssetTransaction, which fails.
        AssetTransactionDTO assetTransactionDTO = assetTransactionMapper.toDto(assetTransaction);

        restAssetTransactionMockMvc.perform(post("/api/asset-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetTransactionDTO)))
            .andExpect(status().isBadRequest());

        List<AssetTransaction> assetTransactionList = assetTransactionRepository.findAll();
        assertThat(assetTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetTransactionRepository.findAll().size();
        // set the field null
        assetTransaction.setTransactionDate(null);

        // Create the AssetTransaction, which fails.
        AssetTransactionDTO assetTransactionDTO = assetTransactionMapper.toDto(assetTransaction);

        restAssetTransactionMockMvc.perform(post("/api/asset-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetTransactionDTO)))
            .andExpect(status().isBadRequest());

        List<AssetTransaction> assetTransactionList = assetTransactionRepository.findAll();
        assertThat(assetTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAssetTransactions() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList
        restAssetTransactionMockMvc.perform(get("/api/asset-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionReference").value(hasItem(DEFAULT_TRANSACTION_REFERENCE)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].scannedDocumentId").value(hasItem(DEFAULT_SCANNED_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].transactionApprovalId").value(hasItem(DEFAULT_TRANSACTION_APPROVAL_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getAssetTransaction() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get the assetTransaction
        restAssetTransactionMockMvc.perform(get("/api/asset-transactions/{id}", assetTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(assetTransaction.getId().intValue()))
            .andExpect(jsonPath("$.transactionReference").value(DEFAULT_TRANSACTION_REFERENCE))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.scannedDocumentId").value(DEFAULT_SCANNED_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.transactionApprovalId").value(DEFAULT_TRANSACTION_APPROVAL_ID.intValue()));
    }


    @Test
    @Transactional
    public void getAssetTransactionsByIdFiltering() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        Long id = assetTransaction.getId();

        defaultAssetTransactionShouldBeFound("id.equals=" + id);
        defaultAssetTransactionShouldNotBeFound("id.notEquals=" + id);

        defaultAssetTransactionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetTransactionShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetTransactionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetTransactionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionReference equals to DEFAULT_TRANSACTION_REFERENCE
        defaultAssetTransactionShouldBeFound("transactionReference.equals=" + DEFAULT_TRANSACTION_REFERENCE);

        // Get all the assetTransactionList where transactionReference equals to UPDATED_TRANSACTION_REFERENCE
        defaultAssetTransactionShouldNotBeFound("transactionReference.equals=" + UPDATED_TRANSACTION_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionReferenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionReference not equals to DEFAULT_TRANSACTION_REFERENCE
        defaultAssetTransactionShouldNotBeFound("transactionReference.notEquals=" + DEFAULT_TRANSACTION_REFERENCE);

        // Get all the assetTransactionList where transactionReference not equals to UPDATED_TRANSACTION_REFERENCE
        defaultAssetTransactionShouldBeFound("transactionReference.notEquals=" + UPDATED_TRANSACTION_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionReference in DEFAULT_TRANSACTION_REFERENCE or UPDATED_TRANSACTION_REFERENCE
        defaultAssetTransactionShouldBeFound("transactionReference.in=" + DEFAULT_TRANSACTION_REFERENCE + "," + UPDATED_TRANSACTION_REFERENCE);

        // Get all the assetTransactionList where transactionReference equals to UPDATED_TRANSACTION_REFERENCE
        defaultAssetTransactionShouldNotBeFound("transactionReference.in=" + UPDATED_TRANSACTION_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionReference is not null
        defaultAssetTransactionShouldBeFound("transactionReference.specified=true");

        // Get all the assetTransactionList where transactionReference is null
        defaultAssetTransactionShouldNotBeFound("transactionReference.specified=false");
    }
                @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionReferenceContainsSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionReference contains DEFAULT_TRANSACTION_REFERENCE
        defaultAssetTransactionShouldBeFound("transactionReference.contains=" + DEFAULT_TRANSACTION_REFERENCE);

        // Get all the assetTransactionList where transactionReference contains UPDATED_TRANSACTION_REFERENCE
        defaultAssetTransactionShouldNotBeFound("transactionReference.contains=" + UPDATED_TRANSACTION_REFERENCE);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionReference does not contain DEFAULT_TRANSACTION_REFERENCE
        defaultAssetTransactionShouldNotBeFound("transactionReference.doesNotContain=" + DEFAULT_TRANSACTION_REFERENCE);

        // Get all the assetTransactionList where transactionReference does not contain UPDATED_TRANSACTION_REFERENCE
        defaultAssetTransactionShouldBeFound("transactionReference.doesNotContain=" + UPDATED_TRANSACTION_REFERENCE);
    }


    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultAssetTransactionShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the assetTransactionList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultAssetTransactionShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultAssetTransactionShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the assetTransactionList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultAssetTransactionShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultAssetTransactionShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the assetTransactionList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultAssetTransactionShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionDate is not null
        defaultAssetTransactionShouldBeFound("transactionDate.specified=true");

        // Get all the assetTransactionList where transactionDate is null
        defaultAssetTransactionShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultAssetTransactionShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the assetTransactionList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultAssetTransactionShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultAssetTransactionShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the assetTransactionList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultAssetTransactionShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultAssetTransactionShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the assetTransactionList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultAssetTransactionShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultAssetTransactionShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the assetTransactionList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultAssetTransactionShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }


    @Test
    @Transactional
    public void getAllAssetTransactionsByScannedDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where scannedDocumentId equals to DEFAULT_SCANNED_DOCUMENT_ID
        defaultAssetTransactionShouldBeFound("scannedDocumentId.equals=" + DEFAULT_SCANNED_DOCUMENT_ID);

        // Get all the assetTransactionList where scannedDocumentId equals to UPDATED_SCANNED_DOCUMENT_ID
        defaultAssetTransactionShouldNotBeFound("scannedDocumentId.equals=" + UPDATED_SCANNED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByScannedDocumentIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where scannedDocumentId not equals to DEFAULT_SCANNED_DOCUMENT_ID
        defaultAssetTransactionShouldNotBeFound("scannedDocumentId.notEquals=" + DEFAULT_SCANNED_DOCUMENT_ID);

        // Get all the assetTransactionList where scannedDocumentId not equals to UPDATED_SCANNED_DOCUMENT_ID
        defaultAssetTransactionShouldBeFound("scannedDocumentId.notEquals=" + UPDATED_SCANNED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByScannedDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where scannedDocumentId in DEFAULT_SCANNED_DOCUMENT_ID or UPDATED_SCANNED_DOCUMENT_ID
        defaultAssetTransactionShouldBeFound("scannedDocumentId.in=" + DEFAULT_SCANNED_DOCUMENT_ID + "," + UPDATED_SCANNED_DOCUMENT_ID);

        // Get all the assetTransactionList where scannedDocumentId equals to UPDATED_SCANNED_DOCUMENT_ID
        defaultAssetTransactionShouldNotBeFound("scannedDocumentId.in=" + UPDATED_SCANNED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByScannedDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where scannedDocumentId is not null
        defaultAssetTransactionShouldBeFound("scannedDocumentId.specified=true");

        // Get all the assetTransactionList where scannedDocumentId is null
        defaultAssetTransactionShouldNotBeFound("scannedDocumentId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByScannedDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where scannedDocumentId is greater than or equal to DEFAULT_SCANNED_DOCUMENT_ID
        defaultAssetTransactionShouldBeFound("scannedDocumentId.greaterThanOrEqual=" + DEFAULT_SCANNED_DOCUMENT_ID);

        // Get all the assetTransactionList where scannedDocumentId is greater than or equal to UPDATED_SCANNED_DOCUMENT_ID
        defaultAssetTransactionShouldNotBeFound("scannedDocumentId.greaterThanOrEqual=" + UPDATED_SCANNED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByScannedDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where scannedDocumentId is less than or equal to DEFAULT_SCANNED_DOCUMENT_ID
        defaultAssetTransactionShouldBeFound("scannedDocumentId.lessThanOrEqual=" + DEFAULT_SCANNED_DOCUMENT_ID);

        // Get all the assetTransactionList where scannedDocumentId is less than or equal to SMALLER_SCANNED_DOCUMENT_ID
        defaultAssetTransactionShouldNotBeFound("scannedDocumentId.lessThanOrEqual=" + SMALLER_SCANNED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByScannedDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where scannedDocumentId is less than DEFAULT_SCANNED_DOCUMENT_ID
        defaultAssetTransactionShouldNotBeFound("scannedDocumentId.lessThan=" + DEFAULT_SCANNED_DOCUMENT_ID);

        // Get all the assetTransactionList where scannedDocumentId is less than UPDATED_SCANNED_DOCUMENT_ID
        defaultAssetTransactionShouldBeFound("scannedDocumentId.lessThan=" + UPDATED_SCANNED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByScannedDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where scannedDocumentId is greater than DEFAULT_SCANNED_DOCUMENT_ID
        defaultAssetTransactionShouldNotBeFound("scannedDocumentId.greaterThan=" + DEFAULT_SCANNED_DOCUMENT_ID);

        // Get all the assetTransactionList where scannedDocumentId is greater than SMALLER_SCANNED_DOCUMENT_ID
        defaultAssetTransactionShouldBeFound("scannedDocumentId.greaterThan=" + SMALLER_SCANNED_DOCUMENT_ID);
    }


    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionApprovalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionApprovalId equals to DEFAULT_TRANSACTION_APPROVAL_ID
        defaultAssetTransactionShouldBeFound("transactionApprovalId.equals=" + DEFAULT_TRANSACTION_APPROVAL_ID);

        // Get all the assetTransactionList where transactionApprovalId equals to UPDATED_TRANSACTION_APPROVAL_ID
        defaultAssetTransactionShouldNotBeFound("transactionApprovalId.equals=" + UPDATED_TRANSACTION_APPROVAL_ID);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionApprovalIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionApprovalId not equals to DEFAULT_TRANSACTION_APPROVAL_ID
        defaultAssetTransactionShouldNotBeFound("transactionApprovalId.notEquals=" + DEFAULT_TRANSACTION_APPROVAL_ID);

        // Get all the assetTransactionList where transactionApprovalId not equals to UPDATED_TRANSACTION_APPROVAL_ID
        defaultAssetTransactionShouldBeFound("transactionApprovalId.notEquals=" + UPDATED_TRANSACTION_APPROVAL_ID);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionApprovalIdIsInShouldWork() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionApprovalId in DEFAULT_TRANSACTION_APPROVAL_ID or UPDATED_TRANSACTION_APPROVAL_ID
        defaultAssetTransactionShouldBeFound("transactionApprovalId.in=" + DEFAULT_TRANSACTION_APPROVAL_ID + "," + UPDATED_TRANSACTION_APPROVAL_ID);

        // Get all the assetTransactionList where transactionApprovalId equals to UPDATED_TRANSACTION_APPROVAL_ID
        defaultAssetTransactionShouldNotBeFound("transactionApprovalId.in=" + UPDATED_TRANSACTION_APPROVAL_ID);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionApprovalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionApprovalId is not null
        defaultAssetTransactionShouldBeFound("transactionApprovalId.specified=true");

        // Get all the assetTransactionList where transactionApprovalId is null
        defaultAssetTransactionShouldNotBeFound("transactionApprovalId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionApprovalIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionApprovalId is greater than or equal to DEFAULT_TRANSACTION_APPROVAL_ID
        defaultAssetTransactionShouldBeFound("transactionApprovalId.greaterThanOrEqual=" + DEFAULT_TRANSACTION_APPROVAL_ID);

        // Get all the assetTransactionList where transactionApprovalId is greater than or equal to UPDATED_TRANSACTION_APPROVAL_ID
        defaultAssetTransactionShouldNotBeFound("transactionApprovalId.greaterThanOrEqual=" + UPDATED_TRANSACTION_APPROVAL_ID);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionApprovalIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionApprovalId is less than or equal to DEFAULT_TRANSACTION_APPROVAL_ID
        defaultAssetTransactionShouldBeFound("transactionApprovalId.lessThanOrEqual=" + DEFAULT_TRANSACTION_APPROVAL_ID);

        // Get all the assetTransactionList where transactionApprovalId is less than or equal to SMALLER_TRANSACTION_APPROVAL_ID
        defaultAssetTransactionShouldNotBeFound("transactionApprovalId.lessThanOrEqual=" + SMALLER_TRANSACTION_APPROVAL_ID);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionApprovalIdIsLessThanSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionApprovalId is less than DEFAULT_TRANSACTION_APPROVAL_ID
        defaultAssetTransactionShouldNotBeFound("transactionApprovalId.lessThan=" + DEFAULT_TRANSACTION_APPROVAL_ID);

        // Get all the assetTransactionList where transactionApprovalId is less than UPDATED_TRANSACTION_APPROVAL_ID
        defaultAssetTransactionShouldBeFound("transactionApprovalId.lessThan=" + UPDATED_TRANSACTION_APPROVAL_ID);
    }

    @Test
    @Transactional
    public void getAllAssetTransactionsByTransactionApprovalIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        // Get all the assetTransactionList where transactionApprovalId is greater than DEFAULT_TRANSACTION_APPROVAL_ID
        defaultAssetTransactionShouldNotBeFound("transactionApprovalId.greaterThan=" + DEFAULT_TRANSACTION_APPROVAL_ID);

        // Get all the assetTransactionList where transactionApprovalId is greater than SMALLER_TRANSACTION_APPROVAL_ID
        defaultAssetTransactionShouldBeFound("transactionApprovalId.greaterThan=" + SMALLER_TRANSACTION_APPROVAL_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetTransactionShouldBeFound(String filter) throws Exception {
        restAssetTransactionMockMvc.perform(get("/api/asset-transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionReference").value(hasItem(DEFAULT_TRANSACTION_REFERENCE)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].scannedDocumentId").value(hasItem(DEFAULT_SCANNED_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].transactionApprovalId").value(hasItem(DEFAULT_TRANSACTION_APPROVAL_ID.intValue())));

        // Check, that the count call also returns 1
        restAssetTransactionMockMvc.perform(get("/api/asset-transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetTransactionShouldNotBeFound(String filter) throws Exception {
        restAssetTransactionMockMvc.perform(get("/api/asset-transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetTransactionMockMvc.perform(get("/api/asset-transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAssetTransaction() throws Exception {
        // Get the assetTransaction
        restAssetTransactionMockMvc.perform(get("/api/asset-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssetTransaction() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        int databaseSizeBeforeUpdate = assetTransactionRepository.findAll().size();

        // Update the assetTransaction
        AssetTransaction updatedAssetTransaction = assetTransactionRepository.findById(assetTransaction.getId()).get();
        // Disconnect from session so that the updates on updatedAssetTransaction are not directly saved in db
        em.detach(updatedAssetTransaction);
        updatedAssetTransaction
            .transactionReference(UPDATED_TRANSACTION_REFERENCE)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .scannedDocumentId(UPDATED_SCANNED_DOCUMENT_ID)
            .transactionApprovalId(UPDATED_TRANSACTION_APPROVAL_ID);
        AssetTransactionDTO assetTransactionDTO = assetTransactionMapper.toDto(updatedAssetTransaction);

        restAssetTransactionMockMvc.perform(put("/api/asset-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetTransactionDTO)))
            .andExpect(status().isOk());

        // Validate the AssetTransaction in the database
        List<AssetTransaction> assetTransactionList = assetTransactionRepository.findAll();
        assertThat(assetTransactionList).hasSize(databaseSizeBeforeUpdate);
        AssetTransaction testAssetTransaction = assetTransactionList.get(assetTransactionList.size() - 1);
        assertThat(testAssetTransaction.getTransactionReference()).isEqualTo(UPDATED_TRANSACTION_REFERENCE);
        assertThat(testAssetTransaction.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testAssetTransaction.getScannedDocumentId()).isEqualTo(UPDATED_SCANNED_DOCUMENT_ID);
        assertThat(testAssetTransaction.getTransactionApprovalId()).isEqualTo(UPDATED_TRANSACTION_APPROVAL_ID);

        // Validate the AssetTransaction in Elasticsearch
        verify(mockAssetTransactionSearchRepository, times(1)).save(testAssetTransaction);
    }

    @Test
    @Transactional
    public void updateNonExistingAssetTransaction() throws Exception {
        int databaseSizeBeforeUpdate = assetTransactionRepository.findAll().size();

        // Create the AssetTransaction
        AssetTransactionDTO assetTransactionDTO = assetTransactionMapper.toDto(assetTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetTransactionMockMvc.perform(put("/api/asset-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AssetTransaction in the database
        List<AssetTransaction> assetTransactionList = assetTransactionRepository.findAll();
        assertThat(assetTransactionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetTransaction in Elasticsearch
        verify(mockAssetTransactionSearchRepository, times(0)).save(assetTransaction);
    }

    @Test
    @Transactional
    public void deleteAssetTransaction() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);

        int databaseSizeBeforeDelete = assetTransactionRepository.findAll().size();

        // Delete the assetTransaction
        restAssetTransactionMockMvc.perform(delete("/api/asset-transactions/{id}", assetTransaction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetTransaction> assetTransactionList = assetTransactionRepository.findAll();
        assertThat(assetTransactionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AssetTransaction in Elasticsearch
        verify(mockAssetTransactionSearchRepository, times(1)).deleteById(assetTransaction.getId());
    }

    @Test
    @Transactional
    public void searchAssetTransaction() throws Exception {
        // Initialize the database
        assetTransactionRepository.saveAndFlush(assetTransaction);
        when(mockAssetTransactionSearchRepository.search(queryStringQuery("id:" + assetTransaction.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(assetTransaction), PageRequest.of(0, 1), 1));
        // Search the assetTransaction
        restAssetTransactionMockMvc.perform(get("/api/_search/asset-transactions?query=id:" + assetTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionReference").value(hasItem(DEFAULT_TRANSACTION_REFERENCE)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].scannedDocumentId").value(hasItem(DEFAULT_SCANNED_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].transactionApprovalId").value(hasItem(DEFAULT_TRANSACTION_APPROVAL_ID.intValue())));
    }
}
