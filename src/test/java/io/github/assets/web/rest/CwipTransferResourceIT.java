package io.github.assets.web.rest;

import io.github.assets.FixedAssetServiceApp;
import io.github.assets.config.SecurityBeanOverrideConfiguration;
import io.github.assets.domain.CwipTransfer;
import io.github.assets.repository.CwipTransferRepository;
import io.github.assets.repository.search.CwipTransferSearchRepository;
import io.github.assets.service.CwipTransferService;
import io.github.assets.service.dto.CwipTransferDTO;
import io.github.assets.service.mapper.CwipTransferMapper;
import io.github.assets.web.rest.errors.ExceptionTranslator;
import io.github.assets.service.dto.CwipTransferCriteria;
import io.github.assets.service.CwipTransferQueryService;

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
 * Integration tests for the {@link CwipTransferResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, FixedAssetServiceApp.class})
public class CwipTransferResourceIT {

    private static final LocalDate DEFAULT_TRANSFER_MONTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSFER_MONTH = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRANSFER_MONTH = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ASSET_SERIAL_TAG = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_SERIAL_TAG = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_OUTLET_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_OUTLET_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_TRANSFER_TRANSACTION_ID = 1L;
    private static final Long UPDATED_TRANSFER_TRANSACTION_ID = 2L;
    private static final Long SMALLER_TRANSFER_TRANSACTION_ID = 1L - 1L;

    private static final Long DEFAULT_ASSET_CATEGORY_ID = 1L;
    private static final Long UPDATED_ASSET_CATEGORY_ID = 2L;
    private static final Long SMALLER_ASSET_CATEGORY_ID = 1L - 1L;

    private static final Long DEFAULT_CWIP_TRANSACTION_ID = 1L;
    private static final Long UPDATED_CWIP_TRANSACTION_ID = 2L;
    private static final Long SMALLER_CWIP_TRANSACTION_ID = 1L - 1L;

    private static final String DEFAULT_TRANSFER_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_TRANSFER_DETAILS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TRANSFER_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRANSFER_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TRANSFER_AMOUNT = new BigDecimal(1 - 1);

    private static final Long DEFAULT_DEALER_ID = 1L;
    private static final Long UPDATED_DEALER_ID = 2L;
    private static final Long SMALLER_DEALER_ID = 1L - 1L;

    private static final Long DEFAULT_TRANSACTION_INVOICE_ID = 1L;
    private static final Long UPDATED_TRANSACTION_INVOICE_ID = 2L;
    private static final Long SMALLER_TRANSACTION_INVOICE_ID = 1L - 1L;

    @Autowired
    private CwipTransferRepository cwipTransferRepository;

    @Autowired
    private CwipTransferMapper cwipTransferMapper;

    @Autowired
    private CwipTransferService cwipTransferService;

    /**
     * This repository is mocked in the io.github.assets.repository.search test package.
     *
     * @see io.github.assets.repository.search.CwipTransferSearchRepositoryMockConfiguration
     */
    @Autowired
    private CwipTransferSearchRepository mockCwipTransferSearchRepository;

    @Autowired
    private CwipTransferQueryService cwipTransferQueryService;

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

    private MockMvc restCwipTransferMockMvc;

    private CwipTransfer cwipTransfer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CwipTransferResource cwipTransferResource = new CwipTransferResource(cwipTransferService, cwipTransferQueryService);
        this.restCwipTransferMockMvc = MockMvcBuilders.standaloneSetup(cwipTransferResource)
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
    public static CwipTransfer createEntity(EntityManager em) {
        CwipTransfer cwipTransfer = new CwipTransfer()
            .transferMonth(DEFAULT_TRANSFER_MONTH)
            .assetSerialTag(DEFAULT_ASSET_SERIAL_TAG)
            .serviceOutletCode(DEFAULT_SERVICE_OUTLET_CODE)
            .transferTransactionId(DEFAULT_TRANSFER_TRANSACTION_ID)
            .assetCategoryId(DEFAULT_ASSET_CATEGORY_ID)
            .cwipTransactionId(DEFAULT_CWIP_TRANSACTION_ID)
            .transferDetails(DEFAULT_TRANSFER_DETAILS)
            .transferAmount(DEFAULT_TRANSFER_AMOUNT)
            .dealerId(DEFAULT_DEALER_ID)
            .transactionInvoiceId(DEFAULT_TRANSACTION_INVOICE_ID);
        return cwipTransfer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CwipTransfer createUpdatedEntity(EntityManager em) {
        CwipTransfer cwipTransfer = new CwipTransfer()
            .transferMonth(UPDATED_TRANSFER_MONTH)
            .assetSerialTag(UPDATED_ASSET_SERIAL_TAG)
            .serviceOutletCode(UPDATED_SERVICE_OUTLET_CODE)
            .transferTransactionId(UPDATED_TRANSFER_TRANSACTION_ID)
            .assetCategoryId(UPDATED_ASSET_CATEGORY_ID)
            .cwipTransactionId(UPDATED_CWIP_TRANSACTION_ID)
            .transferDetails(UPDATED_TRANSFER_DETAILS)
            .transferAmount(UPDATED_TRANSFER_AMOUNT)
            .dealerId(UPDATED_DEALER_ID)
            .transactionInvoiceId(UPDATED_TRANSACTION_INVOICE_ID);
        return cwipTransfer;
    }

    @BeforeEach
    public void initTest() {
        cwipTransfer = createEntity(em);
    }

    @Test
    @Transactional
    public void createCwipTransfer() throws Exception {
        int databaseSizeBeforeCreate = cwipTransferRepository.findAll().size();

        // Create the CwipTransfer
        CwipTransferDTO cwipTransferDTO = cwipTransferMapper.toDto(cwipTransfer);
        restCwipTransferMockMvc.perform(post("/api/cwip-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cwipTransferDTO)))
            .andExpect(status().isCreated());

        // Validate the CwipTransfer in the database
        List<CwipTransfer> cwipTransferList = cwipTransferRepository.findAll();
        assertThat(cwipTransferList).hasSize(databaseSizeBeforeCreate + 1);
        CwipTransfer testCwipTransfer = cwipTransferList.get(cwipTransferList.size() - 1);
        assertThat(testCwipTransfer.getTransferMonth()).isEqualTo(DEFAULT_TRANSFER_MONTH);
        assertThat(testCwipTransfer.getAssetSerialTag()).isEqualTo(DEFAULT_ASSET_SERIAL_TAG);
        assertThat(testCwipTransfer.getServiceOutletCode()).isEqualTo(DEFAULT_SERVICE_OUTLET_CODE);
        assertThat(testCwipTransfer.getTransferTransactionId()).isEqualTo(DEFAULT_TRANSFER_TRANSACTION_ID);
        assertThat(testCwipTransfer.getAssetCategoryId()).isEqualTo(DEFAULT_ASSET_CATEGORY_ID);
        assertThat(testCwipTransfer.getCwipTransactionId()).isEqualTo(DEFAULT_CWIP_TRANSACTION_ID);
        assertThat(testCwipTransfer.getTransferDetails()).isEqualTo(DEFAULT_TRANSFER_DETAILS);
        assertThat(testCwipTransfer.getTransferAmount()).isEqualTo(DEFAULT_TRANSFER_AMOUNT);
        assertThat(testCwipTransfer.getDealerId()).isEqualTo(DEFAULT_DEALER_ID);
        assertThat(testCwipTransfer.getTransactionInvoiceId()).isEqualTo(DEFAULT_TRANSACTION_INVOICE_ID);

        // Validate the CwipTransfer in Elasticsearch
        verify(mockCwipTransferSearchRepository, times(1)).save(testCwipTransfer);
    }

    @Test
    @Transactional
    public void createCwipTransferWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cwipTransferRepository.findAll().size();

        // Create the CwipTransfer with an existing ID
        cwipTransfer.setId(1L);
        CwipTransferDTO cwipTransferDTO = cwipTransferMapper.toDto(cwipTransfer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCwipTransferMockMvc.perform(post("/api/cwip-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cwipTransferDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CwipTransfer in the database
        List<CwipTransfer> cwipTransferList = cwipTransferRepository.findAll();
        assertThat(cwipTransferList).hasSize(databaseSizeBeforeCreate);

        // Validate the CwipTransfer in Elasticsearch
        verify(mockCwipTransferSearchRepository, times(0)).save(cwipTransfer);
    }


    @Test
    @Transactional
    public void checkTransferMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = cwipTransferRepository.findAll().size();
        // set the field null
        cwipTransfer.setTransferMonth(null);

        // Create the CwipTransfer, which fails.
        CwipTransferDTO cwipTransferDTO = cwipTransferMapper.toDto(cwipTransfer);

        restCwipTransferMockMvc.perform(post("/api/cwip-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cwipTransferDTO)))
            .andExpect(status().isBadRequest());

        List<CwipTransfer> cwipTransferList = cwipTransferRepository.findAll();
        assertThat(cwipTransferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAssetSerialTagIsRequired() throws Exception {
        int databaseSizeBeforeTest = cwipTransferRepository.findAll().size();
        // set the field null
        cwipTransfer.setAssetSerialTag(null);

        // Create the CwipTransfer, which fails.
        CwipTransferDTO cwipTransferDTO = cwipTransferMapper.toDto(cwipTransfer);

        restCwipTransferMockMvc.perform(post("/api/cwip-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cwipTransferDTO)))
            .andExpect(status().isBadRequest());

        List<CwipTransfer> cwipTransferList = cwipTransferRepository.findAll();
        assertThat(cwipTransferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServiceOutletCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cwipTransferRepository.findAll().size();
        // set the field null
        cwipTransfer.setServiceOutletCode(null);

        // Create the CwipTransfer, which fails.
        CwipTransferDTO cwipTransferDTO = cwipTransferMapper.toDto(cwipTransfer);

        restCwipTransferMockMvc.perform(post("/api/cwip-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cwipTransferDTO)))
            .andExpect(status().isBadRequest());

        List<CwipTransfer> cwipTransferList = cwipTransferRepository.findAll();
        assertThat(cwipTransferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransferTransactionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = cwipTransferRepository.findAll().size();
        // set the field null
        cwipTransfer.setTransferTransactionId(null);

        // Create the CwipTransfer, which fails.
        CwipTransferDTO cwipTransferDTO = cwipTransferMapper.toDto(cwipTransfer);

        restCwipTransferMockMvc.perform(post("/api/cwip-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cwipTransferDTO)))
            .andExpect(status().isBadRequest());

        List<CwipTransfer> cwipTransferList = cwipTransferRepository.findAll();
        assertThat(cwipTransferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAssetCategoryIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = cwipTransferRepository.findAll().size();
        // set the field null
        cwipTransfer.setAssetCategoryId(null);

        // Create the CwipTransfer, which fails.
        CwipTransferDTO cwipTransferDTO = cwipTransferMapper.toDto(cwipTransfer);

        restCwipTransferMockMvc.perform(post("/api/cwip-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cwipTransferDTO)))
            .andExpect(status().isBadRequest());

        List<CwipTransfer> cwipTransferList = cwipTransferRepository.findAll();
        assertThat(cwipTransferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCwipTransactionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = cwipTransferRepository.findAll().size();
        // set the field null
        cwipTransfer.setCwipTransactionId(null);

        // Create the CwipTransfer, which fails.
        CwipTransferDTO cwipTransferDTO = cwipTransferMapper.toDto(cwipTransfer);

        restCwipTransferMockMvc.perform(post("/api/cwip-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cwipTransferDTO)))
            .andExpect(status().isBadRequest());

        List<CwipTransfer> cwipTransferList = cwipTransferRepository.findAll();
        assertThat(cwipTransferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransferDetailsIsRequired() throws Exception {
        int databaseSizeBeforeTest = cwipTransferRepository.findAll().size();
        // set the field null
        cwipTransfer.setTransferDetails(null);

        // Create the CwipTransfer, which fails.
        CwipTransferDTO cwipTransferDTO = cwipTransferMapper.toDto(cwipTransfer);

        restCwipTransferMockMvc.perform(post("/api/cwip-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cwipTransferDTO)))
            .andExpect(status().isBadRequest());

        List<CwipTransfer> cwipTransferList = cwipTransferRepository.findAll();
        assertThat(cwipTransferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransferAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = cwipTransferRepository.findAll().size();
        // set the field null
        cwipTransfer.setTransferAmount(null);

        // Create the CwipTransfer, which fails.
        CwipTransferDTO cwipTransferDTO = cwipTransferMapper.toDto(cwipTransfer);

        restCwipTransferMockMvc.perform(post("/api/cwip-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cwipTransferDTO)))
            .andExpect(status().isBadRequest());

        List<CwipTransfer> cwipTransferList = cwipTransferRepository.findAll();
        assertThat(cwipTransferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCwipTransfers() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList
        restCwipTransferMockMvc.perform(get("/api/cwip-transfers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cwipTransfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].transferMonth").value(hasItem(DEFAULT_TRANSFER_MONTH.toString())))
            .andExpect(jsonPath("$.[*].assetSerialTag").value(hasItem(DEFAULT_ASSET_SERIAL_TAG)))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].transferTransactionId").value(hasItem(DEFAULT_TRANSFER_TRANSACTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].assetCategoryId").value(hasItem(DEFAULT_ASSET_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].cwipTransactionId").value(hasItem(DEFAULT_CWIP_TRANSACTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].transferDetails").value(hasItem(DEFAULT_TRANSFER_DETAILS)))
            .andExpect(jsonPath("$.[*].transferAmount").value(hasItem(DEFAULT_TRANSFER_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].dealerId").value(hasItem(DEFAULT_DEALER_ID.intValue())))
            .andExpect(jsonPath("$.[*].transactionInvoiceId").value(hasItem(DEFAULT_TRANSACTION_INVOICE_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getCwipTransfer() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get the cwipTransfer
        restCwipTransferMockMvc.perform(get("/api/cwip-transfers/{id}", cwipTransfer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cwipTransfer.getId().intValue()))
            .andExpect(jsonPath("$.transferMonth").value(DEFAULT_TRANSFER_MONTH.toString()))
            .andExpect(jsonPath("$.assetSerialTag").value(DEFAULT_ASSET_SERIAL_TAG))
            .andExpect(jsonPath("$.serviceOutletCode").value(DEFAULT_SERVICE_OUTLET_CODE))
            .andExpect(jsonPath("$.transferTransactionId").value(DEFAULT_TRANSFER_TRANSACTION_ID.intValue()))
            .andExpect(jsonPath("$.assetCategoryId").value(DEFAULT_ASSET_CATEGORY_ID.intValue()))
            .andExpect(jsonPath("$.cwipTransactionId").value(DEFAULT_CWIP_TRANSACTION_ID.intValue()))
            .andExpect(jsonPath("$.transferDetails").value(DEFAULT_TRANSFER_DETAILS))
            .andExpect(jsonPath("$.transferAmount").value(DEFAULT_TRANSFER_AMOUNT.intValue()))
            .andExpect(jsonPath("$.dealerId").value(DEFAULT_DEALER_ID.intValue()))
            .andExpect(jsonPath("$.transactionInvoiceId").value(DEFAULT_TRANSACTION_INVOICE_ID.intValue()));
    }


    @Test
    @Transactional
    public void getCwipTransfersByIdFiltering() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        Long id = cwipTransfer.getId();

        defaultCwipTransferShouldBeFound("id.equals=" + id);
        defaultCwipTransferShouldNotBeFound("id.notEquals=" + id);

        defaultCwipTransferShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCwipTransferShouldNotBeFound("id.greaterThan=" + id);

        defaultCwipTransferShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCwipTransferShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCwipTransfersByTransferMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferMonth equals to DEFAULT_TRANSFER_MONTH
        defaultCwipTransferShouldBeFound("transferMonth.equals=" + DEFAULT_TRANSFER_MONTH);

        // Get all the cwipTransferList where transferMonth equals to UPDATED_TRANSFER_MONTH
        defaultCwipTransferShouldNotBeFound("transferMonth.equals=" + UPDATED_TRANSFER_MONTH);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferMonthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferMonth not equals to DEFAULT_TRANSFER_MONTH
        defaultCwipTransferShouldNotBeFound("transferMonth.notEquals=" + DEFAULT_TRANSFER_MONTH);

        // Get all the cwipTransferList where transferMonth not equals to UPDATED_TRANSFER_MONTH
        defaultCwipTransferShouldBeFound("transferMonth.notEquals=" + UPDATED_TRANSFER_MONTH);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferMonthIsInShouldWork() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferMonth in DEFAULT_TRANSFER_MONTH or UPDATED_TRANSFER_MONTH
        defaultCwipTransferShouldBeFound("transferMonth.in=" + DEFAULT_TRANSFER_MONTH + "," + UPDATED_TRANSFER_MONTH);

        // Get all the cwipTransferList where transferMonth equals to UPDATED_TRANSFER_MONTH
        defaultCwipTransferShouldNotBeFound("transferMonth.in=" + UPDATED_TRANSFER_MONTH);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferMonthIsNullOrNotNull() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferMonth is not null
        defaultCwipTransferShouldBeFound("transferMonth.specified=true");

        // Get all the cwipTransferList where transferMonth is null
        defaultCwipTransferShouldNotBeFound("transferMonth.specified=false");
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferMonthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferMonth is greater than or equal to DEFAULT_TRANSFER_MONTH
        defaultCwipTransferShouldBeFound("transferMonth.greaterThanOrEqual=" + DEFAULT_TRANSFER_MONTH);

        // Get all the cwipTransferList where transferMonth is greater than or equal to UPDATED_TRANSFER_MONTH
        defaultCwipTransferShouldNotBeFound("transferMonth.greaterThanOrEqual=" + UPDATED_TRANSFER_MONTH);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferMonthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferMonth is less than or equal to DEFAULT_TRANSFER_MONTH
        defaultCwipTransferShouldBeFound("transferMonth.lessThanOrEqual=" + DEFAULT_TRANSFER_MONTH);

        // Get all the cwipTransferList where transferMonth is less than or equal to SMALLER_TRANSFER_MONTH
        defaultCwipTransferShouldNotBeFound("transferMonth.lessThanOrEqual=" + SMALLER_TRANSFER_MONTH);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferMonthIsLessThanSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferMonth is less than DEFAULT_TRANSFER_MONTH
        defaultCwipTransferShouldNotBeFound("transferMonth.lessThan=" + DEFAULT_TRANSFER_MONTH);

        // Get all the cwipTransferList where transferMonth is less than UPDATED_TRANSFER_MONTH
        defaultCwipTransferShouldBeFound("transferMonth.lessThan=" + UPDATED_TRANSFER_MONTH);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferMonthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferMonth is greater than DEFAULT_TRANSFER_MONTH
        defaultCwipTransferShouldNotBeFound("transferMonth.greaterThan=" + DEFAULT_TRANSFER_MONTH);

        // Get all the cwipTransferList where transferMonth is greater than SMALLER_TRANSFER_MONTH
        defaultCwipTransferShouldBeFound("transferMonth.greaterThan=" + SMALLER_TRANSFER_MONTH);
    }


    @Test
    @Transactional
    public void getAllCwipTransfersByAssetSerialTagIsEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where assetSerialTag equals to DEFAULT_ASSET_SERIAL_TAG
        defaultCwipTransferShouldBeFound("assetSerialTag.equals=" + DEFAULT_ASSET_SERIAL_TAG);

        // Get all the cwipTransferList where assetSerialTag equals to UPDATED_ASSET_SERIAL_TAG
        defaultCwipTransferShouldNotBeFound("assetSerialTag.equals=" + UPDATED_ASSET_SERIAL_TAG);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByAssetSerialTagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where assetSerialTag not equals to DEFAULT_ASSET_SERIAL_TAG
        defaultCwipTransferShouldNotBeFound("assetSerialTag.notEquals=" + DEFAULT_ASSET_SERIAL_TAG);

        // Get all the cwipTransferList where assetSerialTag not equals to UPDATED_ASSET_SERIAL_TAG
        defaultCwipTransferShouldBeFound("assetSerialTag.notEquals=" + UPDATED_ASSET_SERIAL_TAG);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByAssetSerialTagIsInShouldWork() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where assetSerialTag in DEFAULT_ASSET_SERIAL_TAG or UPDATED_ASSET_SERIAL_TAG
        defaultCwipTransferShouldBeFound("assetSerialTag.in=" + DEFAULT_ASSET_SERIAL_TAG + "," + UPDATED_ASSET_SERIAL_TAG);

        // Get all the cwipTransferList where assetSerialTag equals to UPDATED_ASSET_SERIAL_TAG
        defaultCwipTransferShouldNotBeFound("assetSerialTag.in=" + UPDATED_ASSET_SERIAL_TAG);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByAssetSerialTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where assetSerialTag is not null
        defaultCwipTransferShouldBeFound("assetSerialTag.specified=true");

        // Get all the cwipTransferList where assetSerialTag is null
        defaultCwipTransferShouldNotBeFound("assetSerialTag.specified=false");
    }
                @Test
    @Transactional
    public void getAllCwipTransfersByAssetSerialTagContainsSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where assetSerialTag contains DEFAULT_ASSET_SERIAL_TAG
        defaultCwipTransferShouldBeFound("assetSerialTag.contains=" + DEFAULT_ASSET_SERIAL_TAG);

        // Get all the cwipTransferList where assetSerialTag contains UPDATED_ASSET_SERIAL_TAG
        defaultCwipTransferShouldNotBeFound("assetSerialTag.contains=" + UPDATED_ASSET_SERIAL_TAG);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByAssetSerialTagNotContainsSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where assetSerialTag does not contain DEFAULT_ASSET_SERIAL_TAG
        defaultCwipTransferShouldNotBeFound("assetSerialTag.doesNotContain=" + DEFAULT_ASSET_SERIAL_TAG);

        // Get all the cwipTransferList where assetSerialTag does not contain UPDATED_ASSET_SERIAL_TAG
        defaultCwipTransferShouldBeFound("assetSerialTag.doesNotContain=" + UPDATED_ASSET_SERIAL_TAG);
    }


    @Test
    @Transactional
    public void getAllCwipTransfersByServiceOutletCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where serviceOutletCode equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultCwipTransferShouldBeFound("serviceOutletCode.equals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the cwipTransferList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultCwipTransferShouldNotBeFound("serviceOutletCode.equals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByServiceOutletCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where serviceOutletCode not equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultCwipTransferShouldNotBeFound("serviceOutletCode.notEquals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the cwipTransferList where serviceOutletCode not equals to UPDATED_SERVICE_OUTLET_CODE
        defaultCwipTransferShouldBeFound("serviceOutletCode.notEquals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByServiceOutletCodeIsInShouldWork() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where serviceOutletCode in DEFAULT_SERVICE_OUTLET_CODE or UPDATED_SERVICE_OUTLET_CODE
        defaultCwipTransferShouldBeFound("serviceOutletCode.in=" + DEFAULT_SERVICE_OUTLET_CODE + "," + UPDATED_SERVICE_OUTLET_CODE);

        // Get all the cwipTransferList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultCwipTransferShouldNotBeFound("serviceOutletCode.in=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByServiceOutletCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where serviceOutletCode is not null
        defaultCwipTransferShouldBeFound("serviceOutletCode.specified=true");

        // Get all the cwipTransferList where serviceOutletCode is null
        defaultCwipTransferShouldNotBeFound("serviceOutletCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllCwipTransfersByServiceOutletCodeContainsSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where serviceOutletCode contains DEFAULT_SERVICE_OUTLET_CODE
        defaultCwipTransferShouldBeFound("serviceOutletCode.contains=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the cwipTransferList where serviceOutletCode contains UPDATED_SERVICE_OUTLET_CODE
        defaultCwipTransferShouldNotBeFound("serviceOutletCode.contains=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByServiceOutletCodeNotContainsSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where serviceOutletCode does not contain DEFAULT_SERVICE_OUTLET_CODE
        defaultCwipTransferShouldNotBeFound("serviceOutletCode.doesNotContain=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the cwipTransferList where serviceOutletCode does not contain UPDATED_SERVICE_OUTLET_CODE
        defaultCwipTransferShouldBeFound("serviceOutletCode.doesNotContain=" + UPDATED_SERVICE_OUTLET_CODE);
    }


    @Test
    @Transactional
    public void getAllCwipTransfersByTransferTransactionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferTransactionId equals to DEFAULT_TRANSFER_TRANSACTION_ID
        defaultCwipTransferShouldBeFound("transferTransactionId.equals=" + DEFAULT_TRANSFER_TRANSACTION_ID);

        // Get all the cwipTransferList where transferTransactionId equals to UPDATED_TRANSFER_TRANSACTION_ID
        defaultCwipTransferShouldNotBeFound("transferTransactionId.equals=" + UPDATED_TRANSFER_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferTransactionIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferTransactionId not equals to DEFAULT_TRANSFER_TRANSACTION_ID
        defaultCwipTransferShouldNotBeFound("transferTransactionId.notEquals=" + DEFAULT_TRANSFER_TRANSACTION_ID);

        // Get all the cwipTransferList where transferTransactionId not equals to UPDATED_TRANSFER_TRANSACTION_ID
        defaultCwipTransferShouldBeFound("transferTransactionId.notEquals=" + UPDATED_TRANSFER_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferTransactionIdIsInShouldWork() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferTransactionId in DEFAULT_TRANSFER_TRANSACTION_ID or UPDATED_TRANSFER_TRANSACTION_ID
        defaultCwipTransferShouldBeFound("transferTransactionId.in=" + DEFAULT_TRANSFER_TRANSACTION_ID + "," + UPDATED_TRANSFER_TRANSACTION_ID);

        // Get all the cwipTransferList where transferTransactionId equals to UPDATED_TRANSFER_TRANSACTION_ID
        defaultCwipTransferShouldNotBeFound("transferTransactionId.in=" + UPDATED_TRANSFER_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferTransactionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferTransactionId is not null
        defaultCwipTransferShouldBeFound("transferTransactionId.specified=true");

        // Get all the cwipTransferList where transferTransactionId is null
        defaultCwipTransferShouldNotBeFound("transferTransactionId.specified=false");
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferTransactionIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferTransactionId is greater than or equal to DEFAULT_TRANSFER_TRANSACTION_ID
        defaultCwipTransferShouldBeFound("transferTransactionId.greaterThanOrEqual=" + DEFAULT_TRANSFER_TRANSACTION_ID);

        // Get all the cwipTransferList where transferTransactionId is greater than or equal to UPDATED_TRANSFER_TRANSACTION_ID
        defaultCwipTransferShouldNotBeFound("transferTransactionId.greaterThanOrEqual=" + UPDATED_TRANSFER_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferTransactionIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferTransactionId is less than or equal to DEFAULT_TRANSFER_TRANSACTION_ID
        defaultCwipTransferShouldBeFound("transferTransactionId.lessThanOrEqual=" + DEFAULT_TRANSFER_TRANSACTION_ID);

        // Get all the cwipTransferList where transferTransactionId is less than or equal to SMALLER_TRANSFER_TRANSACTION_ID
        defaultCwipTransferShouldNotBeFound("transferTransactionId.lessThanOrEqual=" + SMALLER_TRANSFER_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferTransactionIdIsLessThanSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferTransactionId is less than DEFAULT_TRANSFER_TRANSACTION_ID
        defaultCwipTransferShouldNotBeFound("transferTransactionId.lessThan=" + DEFAULT_TRANSFER_TRANSACTION_ID);

        // Get all the cwipTransferList where transferTransactionId is less than UPDATED_TRANSFER_TRANSACTION_ID
        defaultCwipTransferShouldBeFound("transferTransactionId.lessThan=" + UPDATED_TRANSFER_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferTransactionIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferTransactionId is greater than DEFAULT_TRANSFER_TRANSACTION_ID
        defaultCwipTransferShouldNotBeFound("transferTransactionId.greaterThan=" + DEFAULT_TRANSFER_TRANSACTION_ID);

        // Get all the cwipTransferList where transferTransactionId is greater than SMALLER_TRANSFER_TRANSACTION_ID
        defaultCwipTransferShouldBeFound("transferTransactionId.greaterThan=" + SMALLER_TRANSFER_TRANSACTION_ID);
    }


    @Test
    @Transactional
    public void getAllCwipTransfersByAssetCategoryIdIsEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where assetCategoryId equals to DEFAULT_ASSET_CATEGORY_ID
        defaultCwipTransferShouldBeFound("assetCategoryId.equals=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the cwipTransferList where assetCategoryId equals to UPDATED_ASSET_CATEGORY_ID
        defaultCwipTransferShouldNotBeFound("assetCategoryId.equals=" + UPDATED_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByAssetCategoryIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where assetCategoryId not equals to DEFAULT_ASSET_CATEGORY_ID
        defaultCwipTransferShouldNotBeFound("assetCategoryId.notEquals=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the cwipTransferList where assetCategoryId not equals to UPDATED_ASSET_CATEGORY_ID
        defaultCwipTransferShouldBeFound("assetCategoryId.notEquals=" + UPDATED_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByAssetCategoryIdIsInShouldWork() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where assetCategoryId in DEFAULT_ASSET_CATEGORY_ID or UPDATED_ASSET_CATEGORY_ID
        defaultCwipTransferShouldBeFound("assetCategoryId.in=" + DEFAULT_ASSET_CATEGORY_ID + "," + UPDATED_ASSET_CATEGORY_ID);

        // Get all the cwipTransferList where assetCategoryId equals to UPDATED_ASSET_CATEGORY_ID
        defaultCwipTransferShouldNotBeFound("assetCategoryId.in=" + UPDATED_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByAssetCategoryIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where assetCategoryId is not null
        defaultCwipTransferShouldBeFound("assetCategoryId.specified=true");

        // Get all the cwipTransferList where assetCategoryId is null
        defaultCwipTransferShouldNotBeFound("assetCategoryId.specified=false");
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByAssetCategoryIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where assetCategoryId is greater than or equal to DEFAULT_ASSET_CATEGORY_ID
        defaultCwipTransferShouldBeFound("assetCategoryId.greaterThanOrEqual=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the cwipTransferList where assetCategoryId is greater than or equal to UPDATED_ASSET_CATEGORY_ID
        defaultCwipTransferShouldNotBeFound("assetCategoryId.greaterThanOrEqual=" + UPDATED_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByAssetCategoryIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where assetCategoryId is less than or equal to DEFAULT_ASSET_CATEGORY_ID
        defaultCwipTransferShouldBeFound("assetCategoryId.lessThanOrEqual=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the cwipTransferList where assetCategoryId is less than or equal to SMALLER_ASSET_CATEGORY_ID
        defaultCwipTransferShouldNotBeFound("assetCategoryId.lessThanOrEqual=" + SMALLER_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByAssetCategoryIdIsLessThanSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where assetCategoryId is less than DEFAULT_ASSET_CATEGORY_ID
        defaultCwipTransferShouldNotBeFound("assetCategoryId.lessThan=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the cwipTransferList where assetCategoryId is less than UPDATED_ASSET_CATEGORY_ID
        defaultCwipTransferShouldBeFound("assetCategoryId.lessThan=" + UPDATED_ASSET_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByAssetCategoryIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where assetCategoryId is greater than DEFAULT_ASSET_CATEGORY_ID
        defaultCwipTransferShouldNotBeFound("assetCategoryId.greaterThan=" + DEFAULT_ASSET_CATEGORY_ID);

        // Get all the cwipTransferList where assetCategoryId is greater than SMALLER_ASSET_CATEGORY_ID
        defaultCwipTransferShouldBeFound("assetCategoryId.greaterThan=" + SMALLER_ASSET_CATEGORY_ID);
    }


    @Test
    @Transactional
    public void getAllCwipTransfersByCwipTransactionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where cwipTransactionId equals to DEFAULT_CWIP_TRANSACTION_ID
        defaultCwipTransferShouldBeFound("cwipTransactionId.equals=" + DEFAULT_CWIP_TRANSACTION_ID);

        // Get all the cwipTransferList where cwipTransactionId equals to UPDATED_CWIP_TRANSACTION_ID
        defaultCwipTransferShouldNotBeFound("cwipTransactionId.equals=" + UPDATED_CWIP_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByCwipTransactionIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where cwipTransactionId not equals to DEFAULT_CWIP_TRANSACTION_ID
        defaultCwipTransferShouldNotBeFound("cwipTransactionId.notEquals=" + DEFAULT_CWIP_TRANSACTION_ID);

        // Get all the cwipTransferList where cwipTransactionId not equals to UPDATED_CWIP_TRANSACTION_ID
        defaultCwipTransferShouldBeFound("cwipTransactionId.notEquals=" + UPDATED_CWIP_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByCwipTransactionIdIsInShouldWork() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where cwipTransactionId in DEFAULT_CWIP_TRANSACTION_ID or UPDATED_CWIP_TRANSACTION_ID
        defaultCwipTransferShouldBeFound("cwipTransactionId.in=" + DEFAULT_CWIP_TRANSACTION_ID + "," + UPDATED_CWIP_TRANSACTION_ID);

        // Get all the cwipTransferList where cwipTransactionId equals to UPDATED_CWIP_TRANSACTION_ID
        defaultCwipTransferShouldNotBeFound("cwipTransactionId.in=" + UPDATED_CWIP_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByCwipTransactionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where cwipTransactionId is not null
        defaultCwipTransferShouldBeFound("cwipTransactionId.specified=true");

        // Get all the cwipTransferList where cwipTransactionId is null
        defaultCwipTransferShouldNotBeFound("cwipTransactionId.specified=false");
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByCwipTransactionIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where cwipTransactionId is greater than or equal to DEFAULT_CWIP_TRANSACTION_ID
        defaultCwipTransferShouldBeFound("cwipTransactionId.greaterThanOrEqual=" + DEFAULT_CWIP_TRANSACTION_ID);

        // Get all the cwipTransferList where cwipTransactionId is greater than or equal to UPDATED_CWIP_TRANSACTION_ID
        defaultCwipTransferShouldNotBeFound("cwipTransactionId.greaterThanOrEqual=" + UPDATED_CWIP_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByCwipTransactionIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where cwipTransactionId is less than or equal to DEFAULT_CWIP_TRANSACTION_ID
        defaultCwipTransferShouldBeFound("cwipTransactionId.lessThanOrEqual=" + DEFAULT_CWIP_TRANSACTION_ID);

        // Get all the cwipTransferList where cwipTransactionId is less than or equal to SMALLER_CWIP_TRANSACTION_ID
        defaultCwipTransferShouldNotBeFound("cwipTransactionId.lessThanOrEqual=" + SMALLER_CWIP_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByCwipTransactionIdIsLessThanSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where cwipTransactionId is less than DEFAULT_CWIP_TRANSACTION_ID
        defaultCwipTransferShouldNotBeFound("cwipTransactionId.lessThan=" + DEFAULT_CWIP_TRANSACTION_ID);

        // Get all the cwipTransferList where cwipTransactionId is less than UPDATED_CWIP_TRANSACTION_ID
        defaultCwipTransferShouldBeFound("cwipTransactionId.lessThan=" + UPDATED_CWIP_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByCwipTransactionIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where cwipTransactionId is greater than DEFAULT_CWIP_TRANSACTION_ID
        defaultCwipTransferShouldNotBeFound("cwipTransactionId.greaterThan=" + DEFAULT_CWIP_TRANSACTION_ID);

        // Get all the cwipTransferList where cwipTransactionId is greater than SMALLER_CWIP_TRANSACTION_ID
        defaultCwipTransferShouldBeFound("cwipTransactionId.greaterThan=" + SMALLER_CWIP_TRANSACTION_ID);
    }


    @Test
    @Transactional
    public void getAllCwipTransfersByTransferDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferDetails equals to DEFAULT_TRANSFER_DETAILS
        defaultCwipTransferShouldBeFound("transferDetails.equals=" + DEFAULT_TRANSFER_DETAILS);

        // Get all the cwipTransferList where transferDetails equals to UPDATED_TRANSFER_DETAILS
        defaultCwipTransferShouldNotBeFound("transferDetails.equals=" + UPDATED_TRANSFER_DETAILS);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferDetails not equals to DEFAULT_TRANSFER_DETAILS
        defaultCwipTransferShouldNotBeFound("transferDetails.notEquals=" + DEFAULT_TRANSFER_DETAILS);

        // Get all the cwipTransferList where transferDetails not equals to UPDATED_TRANSFER_DETAILS
        defaultCwipTransferShouldBeFound("transferDetails.notEquals=" + UPDATED_TRANSFER_DETAILS);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferDetails in DEFAULT_TRANSFER_DETAILS or UPDATED_TRANSFER_DETAILS
        defaultCwipTransferShouldBeFound("transferDetails.in=" + DEFAULT_TRANSFER_DETAILS + "," + UPDATED_TRANSFER_DETAILS);

        // Get all the cwipTransferList where transferDetails equals to UPDATED_TRANSFER_DETAILS
        defaultCwipTransferShouldNotBeFound("transferDetails.in=" + UPDATED_TRANSFER_DETAILS);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferDetails is not null
        defaultCwipTransferShouldBeFound("transferDetails.specified=true");

        // Get all the cwipTransferList where transferDetails is null
        defaultCwipTransferShouldNotBeFound("transferDetails.specified=false");
    }
                @Test
    @Transactional
    public void getAllCwipTransfersByTransferDetailsContainsSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferDetails contains DEFAULT_TRANSFER_DETAILS
        defaultCwipTransferShouldBeFound("transferDetails.contains=" + DEFAULT_TRANSFER_DETAILS);

        // Get all the cwipTransferList where transferDetails contains UPDATED_TRANSFER_DETAILS
        defaultCwipTransferShouldNotBeFound("transferDetails.contains=" + UPDATED_TRANSFER_DETAILS);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferDetails does not contain DEFAULT_TRANSFER_DETAILS
        defaultCwipTransferShouldNotBeFound("transferDetails.doesNotContain=" + DEFAULT_TRANSFER_DETAILS);

        // Get all the cwipTransferList where transferDetails does not contain UPDATED_TRANSFER_DETAILS
        defaultCwipTransferShouldBeFound("transferDetails.doesNotContain=" + UPDATED_TRANSFER_DETAILS);
    }


    @Test
    @Transactional
    public void getAllCwipTransfersByTransferAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferAmount equals to DEFAULT_TRANSFER_AMOUNT
        defaultCwipTransferShouldBeFound("transferAmount.equals=" + DEFAULT_TRANSFER_AMOUNT);

        // Get all the cwipTransferList where transferAmount equals to UPDATED_TRANSFER_AMOUNT
        defaultCwipTransferShouldNotBeFound("transferAmount.equals=" + UPDATED_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferAmount not equals to DEFAULT_TRANSFER_AMOUNT
        defaultCwipTransferShouldNotBeFound("transferAmount.notEquals=" + DEFAULT_TRANSFER_AMOUNT);

        // Get all the cwipTransferList where transferAmount not equals to UPDATED_TRANSFER_AMOUNT
        defaultCwipTransferShouldBeFound("transferAmount.notEquals=" + UPDATED_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferAmountIsInShouldWork() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferAmount in DEFAULT_TRANSFER_AMOUNT or UPDATED_TRANSFER_AMOUNT
        defaultCwipTransferShouldBeFound("transferAmount.in=" + DEFAULT_TRANSFER_AMOUNT + "," + UPDATED_TRANSFER_AMOUNT);

        // Get all the cwipTransferList where transferAmount equals to UPDATED_TRANSFER_AMOUNT
        defaultCwipTransferShouldNotBeFound("transferAmount.in=" + UPDATED_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferAmount is not null
        defaultCwipTransferShouldBeFound("transferAmount.specified=true");

        // Get all the cwipTransferList where transferAmount is null
        defaultCwipTransferShouldNotBeFound("transferAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferAmount is greater than or equal to DEFAULT_TRANSFER_AMOUNT
        defaultCwipTransferShouldBeFound("transferAmount.greaterThanOrEqual=" + DEFAULT_TRANSFER_AMOUNT);

        // Get all the cwipTransferList where transferAmount is greater than or equal to UPDATED_TRANSFER_AMOUNT
        defaultCwipTransferShouldNotBeFound("transferAmount.greaterThanOrEqual=" + UPDATED_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferAmount is less than or equal to DEFAULT_TRANSFER_AMOUNT
        defaultCwipTransferShouldBeFound("transferAmount.lessThanOrEqual=" + DEFAULT_TRANSFER_AMOUNT);

        // Get all the cwipTransferList where transferAmount is less than or equal to SMALLER_TRANSFER_AMOUNT
        defaultCwipTransferShouldNotBeFound("transferAmount.lessThanOrEqual=" + SMALLER_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferAmount is less than DEFAULT_TRANSFER_AMOUNT
        defaultCwipTransferShouldNotBeFound("transferAmount.lessThan=" + DEFAULT_TRANSFER_AMOUNT);

        // Get all the cwipTransferList where transferAmount is less than UPDATED_TRANSFER_AMOUNT
        defaultCwipTransferShouldBeFound("transferAmount.lessThan=" + UPDATED_TRANSFER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransferAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transferAmount is greater than DEFAULT_TRANSFER_AMOUNT
        defaultCwipTransferShouldNotBeFound("transferAmount.greaterThan=" + DEFAULT_TRANSFER_AMOUNT);

        // Get all the cwipTransferList where transferAmount is greater than SMALLER_TRANSFER_AMOUNT
        defaultCwipTransferShouldBeFound("transferAmount.greaterThan=" + SMALLER_TRANSFER_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllCwipTransfersByDealerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where dealerId equals to DEFAULT_DEALER_ID
        defaultCwipTransferShouldBeFound("dealerId.equals=" + DEFAULT_DEALER_ID);

        // Get all the cwipTransferList where dealerId equals to UPDATED_DEALER_ID
        defaultCwipTransferShouldNotBeFound("dealerId.equals=" + UPDATED_DEALER_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByDealerIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where dealerId not equals to DEFAULT_DEALER_ID
        defaultCwipTransferShouldNotBeFound("dealerId.notEquals=" + DEFAULT_DEALER_ID);

        // Get all the cwipTransferList where dealerId not equals to UPDATED_DEALER_ID
        defaultCwipTransferShouldBeFound("dealerId.notEquals=" + UPDATED_DEALER_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByDealerIdIsInShouldWork() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where dealerId in DEFAULT_DEALER_ID or UPDATED_DEALER_ID
        defaultCwipTransferShouldBeFound("dealerId.in=" + DEFAULT_DEALER_ID + "," + UPDATED_DEALER_ID);

        // Get all the cwipTransferList where dealerId equals to UPDATED_DEALER_ID
        defaultCwipTransferShouldNotBeFound("dealerId.in=" + UPDATED_DEALER_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByDealerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where dealerId is not null
        defaultCwipTransferShouldBeFound("dealerId.specified=true");

        // Get all the cwipTransferList where dealerId is null
        defaultCwipTransferShouldNotBeFound("dealerId.specified=false");
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByDealerIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where dealerId is greater than or equal to DEFAULT_DEALER_ID
        defaultCwipTransferShouldBeFound("dealerId.greaterThanOrEqual=" + DEFAULT_DEALER_ID);

        // Get all the cwipTransferList where dealerId is greater than or equal to UPDATED_DEALER_ID
        defaultCwipTransferShouldNotBeFound("dealerId.greaterThanOrEqual=" + UPDATED_DEALER_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByDealerIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where dealerId is less than or equal to DEFAULT_DEALER_ID
        defaultCwipTransferShouldBeFound("dealerId.lessThanOrEqual=" + DEFAULT_DEALER_ID);

        // Get all the cwipTransferList where dealerId is less than or equal to SMALLER_DEALER_ID
        defaultCwipTransferShouldNotBeFound("dealerId.lessThanOrEqual=" + SMALLER_DEALER_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByDealerIdIsLessThanSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where dealerId is less than DEFAULT_DEALER_ID
        defaultCwipTransferShouldNotBeFound("dealerId.lessThan=" + DEFAULT_DEALER_ID);

        // Get all the cwipTransferList where dealerId is less than UPDATED_DEALER_ID
        defaultCwipTransferShouldBeFound("dealerId.lessThan=" + UPDATED_DEALER_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByDealerIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where dealerId is greater than DEFAULT_DEALER_ID
        defaultCwipTransferShouldNotBeFound("dealerId.greaterThan=" + DEFAULT_DEALER_ID);

        // Get all the cwipTransferList where dealerId is greater than SMALLER_DEALER_ID
        defaultCwipTransferShouldBeFound("dealerId.greaterThan=" + SMALLER_DEALER_ID);
    }


    @Test
    @Transactional
    public void getAllCwipTransfersByTransactionInvoiceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transactionInvoiceId equals to DEFAULT_TRANSACTION_INVOICE_ID
        defaultCwipTransferShouldBeFound("transactionInvoiceId.equals=" + DEFAULT_TRANSACTION_INVOICE_ID);

        // Get all the cwipTransferList where transactionInvoiceId equals to UPDATED_TRANSACTION_INVOICE_ID
        defaultCwipTransferShouldNotBeFound("transactionInvoiceId.equals=" + UPDATED_TRANSACTION_INVOICE_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransactionInvoiceIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transactionInvoiceId not equals to DEFAULT_TRANSACTION_INVOICE_ID
        defaultCwipTransferShouldNotBeFound("transactionInvoiceId.notEquals=" + DEFAULT_TRANSACTION_INVOICE_ID);

        // Get all the cwipTransferList where transactionInvoiceId not equals to UPDATED_TRANSACTION_INVOICE_ID
        defaultCwipTransferShouldBeFound("transactionInvoiceId.notEquals=" + UPDATED_TRANSACTION_INVOICE_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransactionInvoiceIdIsInShouldWork() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transactionInvoiceId in DEFAULT_TRANSACTION_INVOICE_ID or UPDATED_TRANSACTION_INVOICE_ID
        defaultCwipTransferShouldBeFound("transactionInvoiceId.in=" + DEFAULT_TRANSACTION_INVOICE_ID + "," + UPDATED_TRANSACTION_INVOICE_ID);

        // Get all the cwipTransferList where transactionInvoiceId equals to UPDATED_TRANSACTION_INVOICE_ID
        defaultCwipTransferShouldNotBeFound("transactionInvoiceId.in=" + UPDATED_TRANSACTION_INVOICE_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransactionInvoiceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transactionInvoiceId is not null
        defaultCwipTransferShouldBeFound("transactionInvoiceId.specified=true");

        // Get all the cwipTransferList where transactionInvoiceId is null
        defaultCwipTransferShouldNotBeFound("transactionInvoiceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransactionInvoiceIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transactionInvoiceId is greater than or equal to DEFAULT_TRANSACTION_INVOICE_ID
        defaultCwipTransferShouldBeFound("transactionInvoiceId.greaterThanOrEqual=" + DEFAULT_TRANSACTION_INVOICE_ID);

        // Get all the cwipTransferList where transactionInvoiceId is greater than or equal to UPDATED_TRANSACTION_INVOICE_ID
        defaultCwipTransferShouldNotBeFound("transactionInvoiceId.greaterThanOrEqual=" + UPDATED_TRANSACTION_INVOICE_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransactionInvoiceIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transactionInvoiceId is less than or equal to DEFAULT_TRANSACTION_INVOICE_ID
        defaultCwipTransferShouldBeFound("transactionInvoiceId.lessThanOrEqual=" + DEFAULT_TRANSACTION_INVOICE_ID);

        // Get all the cwipTransferList where transactionInvoiceId is less than or equal to SMALLER_TRANSACTION_INVOICE_ID
        defaultCwipTransferShouldNotBeFound("transactionInvoiceId.lessThanOrEqual=" + SMALLER_TRANSACTION_INVOICE_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransactionInvoiceIdIsLessThanSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transactionInvoiceId is less than DEFAULT_TRANSACTION_INVOICE_ID
        defaultCwipTransferShouldNotBeFound("transactionInvoiceId.lessThan=" + DEFAULT_TRANSACTION_INVOICE_ID);

        // Get all the cwipTransferList where transactionInvoiceId is less than UPDATED_TRANSACTION_INVOICE_ID
        defaultCwipTransferShouldBeFound("transactionInvoiceId.lessThan=" + UPDATED_TRANSACTION_INVOICE_ID);
    }

    @Test
    @Transactional
    public void getAllCwipTransfersByTransactionInvoiceIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        // Get all the cwipTransferList where transactionInvoiceId is greater than DEFAULT_TRANSACTION_INVOICE_ID
        defaultCwipTransferShouldNotBeFound("transactionInvoiceId.greaterThan=" + DEFAULT_TRANSACTION_INVOICE_ID);

        // Get all the cwipTransferList where transactionInvoiceId is greater than SMALLER_TRANSACTION_INVOICE_ID
        defaultCwipTransferShouldBeFound("transactionInvoiceId.greaterThan=" + SMALLER_TRANSACTION_INVOICE_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCwipTransferShouldBeFound(String filter) throws Exception {
        restCwipTransferMockMvc.perform(get("/api/cwip-transfers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cwipTransfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].transferMonth").value(hasItem(DEFAULT_TRANSFER_MONTH.toString())))
            .andExpect(jsonPath("$.[*].assetSerialTag").value(hasItem(DEFAULT_ASSET_SERIAL_TAG)))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].transferTransactionId").value(hasItem(DEFAULT_TRANSFER_TRANSACTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].assetCategoryId").value(hasItem(DEFAULT_ASSET_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].cwipTransactionId").value(hasItem(DEFAULT_CWIP_TRANSACTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].transferDetails").value(hasItem(DEFAULT_TRANSFER_DETAILS)))
            .andExpect(jsonPath("$.[*].transferAmount").value(hasItem(DEFAULT_TRANSFER_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].dealerId").value(hasItem(DEFAULT_DEALER_ID.intValue())))
            .andExpect(jsonPath("$.[*].transactionInvoiceId").value(hasItem(DEFAULT_TRANSACTION_INVOICE_ID.intValue())));

        // Check, that the count call also returns 1
        restCwipTransferMockMvc.perform(get("/api/cwip-transfers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCwipTransferShouldNotBeFound(String filter) throws Exception {
        restCwipTransferMockMvc.perform(get("/api/cwip-transfers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCwipTransferMockMvc.perform(get("/api/cwip-transfers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCwipTransfer() throws Exception {
        // Get the cwipTransfer
        restCwipTransferMockMvc.perform(get("/api/cwip-transfers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCwipTransfer() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        int databaseSizeBeforeUpdate = cwipTransferRepository.findAll().size();

        // Update the cwipTransfer
        CwipTransfer updatedCwipTransfer = cwipTransferRepository.findById(cwipTransfer.getId()).get();
        // Disconnect from session so that the updates on updatedCwipTransfer are not directly saved in db
        em.detach(updatedCwipTransfer);
        updatedCwipTransfer
            .transferMonth(UPDATED_TRANSFER_MONTH)
            .assetSerialTag(UPDATED_ASSET_SERIAL_TAG)
            .serviceOutletCode(UPDATED_SERVICE_OUTLET_CODE)
            .transferTransactionId(UPDATED_TRANSFER_TRANSACTION_ID)
            .assetCategoryId(UPDATED_ASSET_CATEGORY_ID)
            .cwipTransactionId(UPDATED_CWIP_TRANSACTION_ID)
            .transferDetails(UPDATED_TRANSFER_DETAILS)
            .transferAmount(UPDATED_TRANSFER_AMOUNT)
            .dealerId(UPDATED_DEALER_ID)
            .transactionInvoiceId(UPDATED_TRANSACTION_INVOICE_ID);
        CwipTransferDTO cwipTransferDTO = cwipTransferMapper.toDto(updatedCwipTransfer);

        restCwipTransferMockMvc.perform(put("/api/cwip-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cwipTransferDTO)))
            .andExpect(status().isOk());

        // Validate the CwipTransfer in the database
        List<CwipTransfer> cwipTransferList = cwipTransferRepository.findAll();
        assertThat(cwipTransferList).hasSize(databaseSizeBeforeUpdate);
        CwipTransfer testCwipTransfer = cwipTransferList.get(cwipTransferList.size() - 1);
        assertThat(testCwipTransfer.getTransferMonth()).isEqualTo(UPDATED_TRANSFER_MONTH);
        assertThat(testCwipTransfer.getAssetSerialTag()).isEqualTo(UPDATED_ASSET_SERIAL_TAG);
        assertThat(testCwipTransfer.getServiceOutletCode()).isEqualTo(UPDATED_SERVICE_OUTLET_CODE);
        assertThat(testCwipTransfer.getTransferTransactionId()).isEqualTo(UPDATED_TRANSFER_TRANSACTION_ID);
        assertThat(testCwipTransfer.getAssetCategoryId()).isEqualTo(UPDATED_ASSET_CATEGORY_ID);
        assertThat(testCwipTransfer.getCwipTransactionId()).isEqualTo(UPDATED_CWIP_TRANSACTION_ID);
        assertThat(testCwipTransfer.getTransferDetails()).isEqualTo(UPDATED_TRANSFER_DETAILS);
        assertThat(testCwipTransfer.getTransferAmount()).isEqualTo(UPDATED_TRANSFER_AMOUNT);
        assertThat(testCwipTransfer.getDealerId()).isEqualTo(UPDATED_DEALER_ID);
        assertThat(testCwipTransfer.getTransactionInvoiceId()).isEqualTo(UPDATED_TRANSACTION_INVOICE_ID);

        // Validate the CwipTransfer in Elasticsearch
        verify(mockCwipTransferSearchRepository, times(1)).save(testCwipTransfer);
    }

    @Test
    @Transactional
    public void updateNonExistingCwipTransfer() throws Exception {
        int databaseSizeBeforeUpdate = cwipTransferRepository.findAll().size();

        // Create the CwipTransfer
        CwipTransferDTO cwipTransferDTO = cwipTransferMapper.toDto(cwipTransfer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCwipTransferMockMvc.perform(put("/api/cwip-transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cwipTransferDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CwipTransfer in the database
        List<CwipTransfer> cwipTransferList = cwipTransferRepository.findAll();
        assertThat(cwipTransferList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CwipTransfer in Elasticsearch
        verify(mockCwipTransferSearchRepository, times(0)).save(cwipTransfer);
    }

    @Test
    @Transactional
    public void deleteCwipTransfer() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);

        int databaseSizeBeforeDelete = cwipTransferRepository.findAll().size();

        // Delete the cwipTransfer
        restCwipTransferMockMvc.perform(delete("/api/cwip-transfers/{id}", cwipTransfer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CwipTransfer> cwipTransferList = cwipTransferRepository.findAll();
        assertThat(cwipTransferList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CwipTransfer in Elasticsearch
        verify(mockCwipTransferSearchRepository, times(1)).deleteById(cwipTransfer.getId());
    }

    @Test
    @Transactional
    public void searchCwipTransfer() throws Exception {
        // Initialize the database
        cwipTransferRepository.saveAndFlush(cwipTransfer);
        when(mockCwipTransferSearchRepository.search(queryStringQuery("id:" + cwipTransfer.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cwipTransfer), PageRequest.of(0, 1), 1));
        // Search the cwipTransfer
        restCwipTransferMockMvc.perform(get("/api/_search/cwip-transfers?query=id:" + cwipTransfer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cwipTransfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].transferMonth").value(hasItem(DEFAULT_TRANSFER_MONTH.toString())))
            .andExpect(jsonPath("$.[*].assetSerialTag").value(hasItem(DEFAULT_ASSET_SERIAL_TAG)))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].transferTransactionId").value(hasItem(DEFAULT_TRANSFER_TRANSACTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].assetCategoryId").value(hasItem(DEFAULT_ASSET_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].cwipTransactionId").value(hasItem(DEFAULT_CWIP_TRANSACTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].transferDetails").value(hasItem(DEFAULT_TRANSFER_DETAILS)))
            .andExpect(jsonPath("$.[*].transferAmount").value(hasItem(DEFAULT_TRANSFER_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].dealerId").value(hasItem(DEFAULT_DEALER_ID.intValue())))
            .andExpect(jsonPath("$.[*].transactionInvoiceId").value(hasItem(DEFAULT_TRANSACTION_INVOICE_ID.intValue())));
    }
}
