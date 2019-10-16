package io.github.assets.web.rest;

import io.github.assets.FixedAssetServiceApp;
import io.github.assets.config.SecurityBeanOverrideConfiguration;
import io.github.assets.domain.Dealer;
import io.github.assets.domain.FixedAssetInvoice;
import io.github.assets.repository.DealerRepository;
import io.github.assets.repository.search.DealerSearchRepository;
import io.github.assets.service.DealerService;
import io.github.assets.service.dto.DealerDTO;
import io.github.assets.service.mapper.DealerMapper;
import io.github.assets.web.rest.errors.ExceptionTranslator;
import io.github.assets.service.dto.DealerCriteria;
import io.github.assets.service.DealerQueryService;

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
import java.util.Collections;
import java.util.List;

import static io.github.assets.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.assets.domain.enumeration.TitleTypes;
/**
 * Integration tests for the {@link DealerResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, FixedAssetServiceApp.class})
public class DealerResourceIT {

    private static final TitleTypes DEFAULT_TITLE = TitleTypes.DR;
    private static final TitleTypes UPDATED_TITLE = TitleTypes.ENG;

    private static final String DEFAULT_DEALER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEALER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEALER_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_DEALER_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_DEALER_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DEALER_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DEALER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_DEALER_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BANK_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_BRANCH = "AAAAAAAAAA";
    private static final String UPDATED_BANK_BRANCH = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_SWIFT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BANK_SWIFT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_PHYSICAL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_BANK_PHYSICAL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_DOMICILE = "AAAAAAAAAA";
    private static final String UPDATED_DOMICILE = "BBBBBBBBBB";

    private static final String DEFAULT_TAX_AUTHORITY_REF = "AAAAAAAAAA";
    private static final String UPDATED_TAX_AUTHORITY_REF = "BBBBBBBBBB";

    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private DealerMapper dealerMapper;

    @Autowired
    private DealerService dealerService;

    /**
     * This repository is mocked in the io.github.assets.repository.search test package.
     *
     * @see io.github.assets.repository.search.DealerSearchRepositoryMockConfiguration
     */
    @Autowired
    private DealerSearchRepository mockDealerSearchRepository;

    @Autowired
    private DealerQueryService dealerQueryService;

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

    private MockMvc restDealerMockMvc;

    private Dealer dealer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DealerResource dealerResource = new DealerResource(dealerService, dealerQueryService);
        this.restDealerMockMvc = MockMvcBuilders.standaloneSetup(dealerResource)
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
    public static Dealer createEntity(EntityManager em) {
        Dealer dealer = new Dealer()
            .title(DEFAULT_TITLE)
            .dealerName(DEFAULT_DEALER_NAME)
            .dealerAddress(DEFAULT_DEALER_ADDRESS)
            .dealerPhoneNumber(DEFAULT_DEALER_PHONE_NUMBER)
            .dealerEmail(DEFAULT_DEALER_EMAIL)
            .bankName(DEFAULT_BANK_NAME)
            .bankAccountNumber(DEFAULT_BANK_ACCOUNT_NUMBER)
            .bankBranch(DEFAULT_BANK_BRANCH)
            .bankSwiftCode(DEFAULT_BANK_SWIFT_CODE)
            .bankPhysicalAddress(DEFAULT_BANK_PHYSICAL_ADDRESS)
            .domicile(DEFAULT_DOMICILE)
            .taxAuthorityRef(DEFAULT_TAX_AUTHORITY_REF);
        return dealer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dealer createUpdatedEntity(EntityManager em) {
        Dealer dealer = new Dealer()
            .title(UPDATED_TITLE)
            .dealerName(UPDATED_DEALER_NAME)
            .dealerAddress(UPDATED_DEALER_ADDRESS)
            .dealerPhoneNumber(UPDATED_DEALER_PHONE_NUMBER)
            .dealerEmail(UPDATED_DEALER_EMAIL)
            .bankName(UPDATED_BANK_NAME)
            .bankAccountNumber(UPDATED_BANK_ACCOUNT_NUMBER)
            .bankBranch(UPDATED_BANK_BRANCH)
            .bankSwiftCode(UPDATED_BANK_SWIFT_CODE)
            .bankPhysicalAddress(UPDATED_BANK_PHYSICAL_ADDRESS)
            .domicile(UPDATED_DOMICILE)
            .taxAuthorityRef(UPDATED_TAX_AUTHORITY_REF);
        return dealer;
    }

    @BeforeEach
    public void initTest() {
        dealer = createEntity(em);
    }

    @Test
    @Transactional
    public void createDealer() throws Exception {
        int databaseSizeBeforeCreate = dealerRepository.findAll().size();

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);
        restDealerMockMvc.perform(post("/api/dealers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dealerDTO)))
            .andExpect(status().isCreated());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeCreate + 1);
        Dealer testDealer = dealerList.get(dealerList.size() - 1);
        assertThat(testDealer.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDealer.getDealerName()).isEqualTo(DEFAULT_DEALER_NAME);
        assertThat(testDealer.getDealerAddress()).isEqualTo(DEFAULT_DEALER_ADDRESS);
        assertThat(testDealer.getDealerPhoneNumber()).isEqualTo(DEFAULT_DEALER_PHONE_NUMBER);
        assertThat(testDealer.getDealerEmail()).isEqualTo(DEFAULT_DEALER_EMAIL);
        assertThat(testDealer.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
        assertThat(testDealer.getBankAccountNumber()).isEqualTo(DEFAULT_BANK_ACCOUNT_NUMBER);
        assertThat(testDealer.getBankBranch()).isEqualTo(DEFAULT_BANK_BRANCH);
        assertThat(testDealer.getBankSwiftCode()).isEqualTo(DEFAULT_BANK_SWIFT_CODE);
        assertThat(testDealer.getBankPhysicalAddress()).isEqualTo(DEFAULT_BANK_PHYSICAL_ADDRESS);
        assertThat(testDealer.getDomicile()).isEqualTo(DEFAULT_DOMICILE);
        assertThat(testDealer.getTaxAuthorityRef()).isEqualTo(DEFAULT_TAX_AUTHORITY_REF);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(1)).save(testDealer);
    }

    @Test
    @Transactional
    public void createDealerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dealerRepository.findAll().size();

        // Create the Dealer with an existing ID
        dealer.setId(1L);
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDealerMockMvc.perform(post("/api/dealers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dealerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(0)).save(dealer);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = dealerRepository.findAll().size();
        // set the field null
        dealer.setTitle(null);

        // Create the Dealer, which fails.
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        restDealerMockMvc.perform(post("/api/dealers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dealerDTO)))
            .andExpect(status().isBadRequest());

        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDealerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dealerRepository.findAll().size();
        // set the field null
        dealer.setDealerName(null);

        // Create the Dealer, which fails.
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        restDealerMockMvc.perform(post("/api/dealers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dealerDTO)))
            .andExpect(status().isBadRequest());

        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDealers() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList
        restDealerMockMvc.perform(get("/api/dealers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealer.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].dealerAddress").value(hasItem(DEFAULT_DEALER_ADDRESS)))
            .andExpect(jsonPath("$.[*].dealerPhoneNumber").value(hasItem(DEFAULT_DEALER_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].dealerEmail").value(hasItem(DEFAULT_DEALER_EMAIL)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].bankAccountNumber").value(hasItem(DEFAULT_BANK_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].bankBranch").value(hasItem(DEFAULT_BANK_BRANCH)))
            .andExpect(jsonPath("$.[*].bankSwiftCode").value(hasItem(DEFAULT_BANK_SWIFT_CODE)))
            .andExpect(jsonPath("$.[*].bankPhysicalAddress").value(hasItem(DEFAULT_BANK_PHYSICAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].domicile").value(hasItem(DEFAULT_DOMICILE)))
            .andExpect(jsonPath("$.[*].taxAuthorityRef").value(hasItem(DEFAULT_TAX_AUTHORITY_REF)));
    }
    
    @Test
    @Transactional
    public void getDealer() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get the dealer
        restDealerMockMvc.perform(get("/api/dealers/{id}", dealer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dealer.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.dealerName").value(DEFAULT_DEALER_NAME))
            .andExpect(jsonPath("$.dealerAddress").value(DEFAULT_DEALER_ADDRESS))
            .andExpect(jsonPath("$.dealerPhoneNumber").value(DEFAULT_DEALER_PHONE_NUMBER))
            .andExpect(jsonPath("$.dealerEmail").value(DEFAULT_DEALER_EMAIL))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME))
            .andExpect(jsonPath("$.bankAccountNumber").value(DEFAULT_BANK_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.bankBranch").value(DEFAULT_BANK_BRANCH))
            .andExpect(jsonPath("$.bankSwiftCode").value(DEFAULT_BANK_SWIFT_CODE))
            .andExpect(jsonPath("$.bankPhysicalAddress").value(DEFAULT_BANK_PHYSICAL_ADDRESS))
            .andExpect(jsonPath("$.domicile").value(DEFAULT_DOMICILE))
            .andExpect(jsonPath("$.taxAuthorityRef").value(DEFAULT_TAX_AUTHORITY_REF));
    }

    @Test
    @Transactional
    public void getAllDealersByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where title equals to DEFAULT_TITLE
        defaultDealerShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the dealerList where title equals to UPDATED_TITLE
        defaultDealerShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllDealersByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where title not equals to DEFAULT_TITLE
        defaultDealerShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the dealerList where title not equals to UPDATED_TITLE
        defaultDealerShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllDealersByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultDealerShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the dealerList where title equals to UPDATED_TITLE
        defaultDealerShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllDealersByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where title is not null
        defaultDealerShouldBeFound("title.specified=true");

        // Get all the dealerList where title is null
        defaultDealerShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllDealersByDealerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName equals to DEFAULT_DEALER_NAME
        defaultDealerShouldBeFound("dealerName.equals=" + DEFAULT_DEALER_NAME);

        // Get all the dealerList where dealerName equals to UPDATED_DEALER_NAME
        defaultDealerShouldNotBeFound("dealerName.equals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName not equals to DEFAULT_DEALER_NAME
        defaultDealerShouldNotBeFound("dealerName.notEquals=" + DEFAULT_DEALER_NAME);

        // Get all the dealerList where dealerName not equals to UPDATED_DEALER_NAME
        defaultDealerShouldBeFound("dealerName.notEquals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerNameIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName in DEFAULT_DEALER_NAME or UPDATED_DEALER_NAME
        defaultDealerShouldBeFound("dealerName.in=" + DEFAULT_DEALER_NAME + "," + UPDATED_DEALER_NAME);

        // Get all the dealerList where dealerName equals to UPDATED_DEALER_NAME
        defaultDealerShouldNotBeFound("dealerName.in=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName is not null
        defaultDealerShouldBeFound("dealerName.specified=true");

        // Get all the dealerList where dealerName is null
        defaultDealerShouldNotBeFound("dealerName.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByDealerNameContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName contains DEFAULT_DEALER_NAME
        defaultDealerShouldBeFound("dealerName.contains=" + DEFAULT_DEALER_NAME);

        // Get all the dealerList where dealerName contains UPDATED_DEALER_NAME
        defaultDealerShouldNotBeFound("dealerName.contains=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerNameNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName does not contain DEFAULT_DEALER_NAME
        defaultDealerShouldNotBeFound("dealerName.doesNotContain=" + DEFAULT_DEALER_NAME);

        // Get all the dealerList where dealerName does not contain UPDATED_DEALER_NAME
        defaultDealerShouldBeFound("dealerName.doesNotContain=" + UPDATED_DEALER_NAME);
    }


    @Test
    @Transactional
    public void getAllDealersByDealerAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerAddress equals to DEFAULT_DEALER_ADDRESS
        defaultDealerShouldBeFound("dealerAddress.equals=" + DEFAULT_DEALER_ADDRESS);

        // Get all the dealerList where dealerAddress equals to UPDATED_DEALER_ADDRESS
        defaultDealerShouldNotBeFound("dealerAddress.equals=" + UPDATED_DEALER_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerAddress not equals to DEFAULT_DEALER_ADDRESS
        defaultDealerShouldNotBeFound("dealerAddress.notEquals=" + DEFAULT_DEALER_ADDRESS);

        // Get all the dealerList where dealerAddress not equals to UPDATED_DEALER_ADDRESS
        defaultDealerShouldBeFound("dealerAddress.notEquals=" + UPDATED_DEALER_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerAddressIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerAddress in DEFAULT_DEALER_ADDRESS or UPDATED_DEALER_ADDRESS
        defaultDealerShouldBeFound("dealerAddress.in=" + DEFAULT_DEALER_ADDRESS + "," + UPDATED_DEALER_ADDRESS);

        // Get all the dealerList where dealerAddress equals to UPDATED_DEALER_ADDRESS
        defaultDealerShouldNotBeFound("dealerAddress.in=" + UPDATED_DEALER_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerAddress is not null
        defaultDealerShouldBeFound("dealerAddress.specified=true");

        // Get all the dealerList where dealerAddress is null
        defaultDealerShouldNotBeFound("dealerAddress.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByDealerAddressContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerAddress contains DEFAULT_DEALER_ADDRESS
        defaultDealerShouldBeFound("dealerAddress.contains=" + DEFAULT_DEALER_ADDRESS);

        // Get all the dealerList where dealerAddress contains UPDATED_DEALER_ADDRESS
        defaultDealerShouldNotBeFound("dealerAddress.contains=" + UPDATED_DEALER_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerAddressNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerAddress does not contain DEFAULT_DEALER_ADDRESS
        defaultDealerShouldNotBeFound("dealerAddress.doesNotContain=" + DEFAULT_DEALER_ADDRESS);

        // Get all the dealerList where dealerAddress does not contain UPDATED_DEALER_ADDRESS
        defaultDealerShouldBeFound("dealerAddress.doesNotContain=" + UPDATED_DEALER_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllDealersByDealerPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerPhoneNumber equals to DEFAULT_DEALER_PHONE_NUMBER
        defaultDealerShouldBeFound("dealerPhoneNumber.equals=" + DEFAULT_DEALER_PHONE_NUMBER);

        // Get all the dealerList where dealerPhoneNumber equals to UPDATED_DEALER_PHONE_NUMBER
        defaultDealerShouldNotBeFound("dealerPhoneNumber.equals=" + UPDATED_DEALER_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerPhoneNumber not equals to DEFAULT_DEALER_PHONE_NUMBER
        defaultDealerShouldNotBeFound("dealerPhoneNumber.notEquals=" + DEFAULT_DEALER_PHONE_NUMBER);

        // Get all the dealerList where dealerPhoneNumber not equals to UPDATED_DEALER_PHONE_NUMBER
        defaultDealerShouldBeFound("dealerPhoneNumber.notEquals=" + UPDATED_DEALER_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerPhoneNumber in DEFAULT_DEALER_PHONE_NUMBER or UPDATED_DEALER_PHONE_NUMBER
        defaultDealerShouldBeFound("dealerPhoneNumber.in=" + DEFAULT_DEALER_PHONE_NUMBER + "," + UPDATED_DEALER_PHONE_NUMBER);

        // Get all the dealerList where dealerPhoneNumber equals to UPDATED_DEALER_PHONE_NUMBER
        defaultDealerShouldNotBeFound("dealerPhoneNumber.in=" + UPDATED_DEALER_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerPhoneNumber is not null
        defaultDealerShouldBeFound("dealerPhoneNumber.specified=true");

        // Get all the dealerList where dealerPhoneNumber is null
        defaultDealerShouldNotBeFound("dealerPhoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByDealerPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerPhoneNumber contains DEFAULT_DEALER_PHONE_NUMBER
        defaultDealerShouldBeFound("dealerPhoneNumber.contains=" + DEFAULT_DEALER_PHONE_NUMBER);

        // Get all the dealerList where dealerPhoneNumber contains UPDATED_DEALER_PHONE_NUMBER
        defaultDealerShouldNotBeFound("dealerPhoneNumber.contains=" + UPDATED_DEALER_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerPhoneNumber does not contain DEFAULT_DEALER_PHONE_NUMBER
        defaultDealerShouldNotBeFound("dealerPhoneNumber.doesNotContain=" + DEFAULT_DEALER_PHONE_NUMBER);

        // Get all the dealerList where dealerPhoneNumber does not contain UPDATED_DEALER_PHONE_NUMBER
        defaultDealerShouldBeFound("dealerPhoneNumber.doesNotContain=" + UPDATED_DEALER_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllDealersByDealerEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerEmail equals to DEFAULT_DEALER_EMAIL
        defaultDealerShouldBeFound("dealerEmail.equals=" + DEFAULT_DEALER_EMAIL);

        // Get all the dealerList where dealerEmail equals to UPDATED_DEALER_EMAIL
        defaultDealerShouldNotBeFound("dealerEmail.equals=" + UPDATED_DEALER_EMAIL);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerEmail not equals to DEFAULT_DEALER_EMAIL
        defaultDealerShouldNotBeFound("dealerEmail.notEquals=" + DEFAULT_DEALER_EMAIL);

        // Get all the dealerList where dealerEmail not equals to UPDATED_DEALER_EMAIL
        defaultDealerShouldBeFound("dealerEmail.notEquals=" + UPDATED_DEALER_EMAIL);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerEmailIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerEmail in DEFAULT_DEALER_EMAIL or UPDATED_DEALER_EMAIL
        defaultDealerShouldBeFound("dealerEmail.in=" + DEFAULT_DEALER_EMAIL + "," + UPDATED_DEALER_EMAIL);

        // Get all the dealerList where dealerEmail equals to UPDATED_DEALER_EMAIL
        defaultDealerShouldNotBeFound("dealerEmail.in=" + UPDATED_DEALER_EMAIL);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerEmail is not null
        defaultDealerShouldBeFound("dealerEmail.specified=true");

        // Get all the dealerList where dealerEmail is null
        defaultDealerShouldNotBeFound("dealerEmail.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByDealerEmailContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerEmail contains DEFAULT_DEALER_EMAIL
        defaultDealerShouldBeFound("dealerEmail.contains=" + DEFAULT_DEALER_EMAIL);

        // Get all the dealerList where dealerEmail contains UPDATED_DEALER_EMAIL
        defaultDealerShouldNotBeFound("dealerEmail.contains=" + UPDATED_DEALER_EMAIL);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerEmailNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerEmail does not contain DEFAULT_DEALER_EMAIL
        defaultDealerShouldNotBeFound("dealerEmail.doesNotContain=" + DEFAULT_DEALER_EMAIL);

        // Get all the dealerList where dealerEmail does not contain UPDATED_DEALER_EMAIL
        defaultDealerShouldBeFound("dealerEmail.doesNotContain=" + UPDATED_DEALER_EMAIL);
    }


    @Test
    @Transactional
    public void getAllDealersByBankNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankName equals to DEFAULT_BANK_NAME
        defaultDealerShouldBeFound("bankName.equals=" + DEFAULT_BANK_NAME);

        // Get all the dealerList where bankName equals to UPDATED_BANK_NAME
        defaultDealerShouldNotBeFound("bankName.equals=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByBankNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankName not equals to DEFAULT_BANK_NAME
        defaultDealerShouldNotBeFound("bankName.notEquals=" + DEFAULT_BANK_NAME);

        // Get all the dealerList where bankName not equals to UPDATED_BANK_NAME
        defaultDealerShouldBeFound("bankName.notEquals=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByBankNameIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankName in DEFAULT_BANK_NAME or UPDATED_BANK_NAME
        defaultDealerShouldBeFound("bankName.in=" + DEFAULT_BANK_NAME + "," + UPDATED_BANK_NAME);

        // Get all the dealerList where bankName equals to UPDATED_BANK_NAME
        defaultDealerShouldNotBeFound("bankName.in=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByBankNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankName is not null
        defaultDealerShouldBeFound("bankName.specified=true");

        // Get all the dealerList where bankName is null
        defaultDealerShouldNotBeFound("bankName.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByBankNameContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankName contains DEFAULT_BANK_NAME
        defaultDealerShouldBeFound("bankName.contains=" + DEFAULT_BANK_NAME);

        // Get all the dealerList where bankName contains UPDATED_BANK_NAME
        defaultDealerShouldNotBeFound("bankName.contains=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByBankNameNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankName does not contain DEFAULT_BANK_NAME
        defaultDealerShouldNotBeFound("bankName.doesNotContain=" + DEFAULT_BANK_NAME);

        // Get all the dealerList where bankName does not contain UPDATED_BANK_NAME
        defaultDealerShouldBeFound("bankName.doesNotContain=" + UPDATED_BANK_NAME);
    }


    @Test
    @Transactional
    public void getAllDealersByBankAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankAccountNumber equals to DEFAULT_BANK_ACCOUNT_NUMBER
        defaultDealerShouldBeFound("bankAccountNumber.equals=" + DEFAULT_BANK_ACCOUNT_NUMBER);

        // Get all the dealerList where bankAccountNumber equals to UPDATED_BANK_ACCOUNT_NUMBER
        defaultDealerShouldNotBeFound("bankAccountNumber.equals=" + UPDATED_BANK_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDealersByBankAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankAccountNumber not equals to DEFAULT_BANK_ACCOUNT_NUMBER
        defaultDealerShouldNotBeFound("bankAccountNumber.notEquals=" + DEFAULT_BANK_ACCOUNT_NUMBER);

        // Get all the dealerList where bankAccountNumber not equals to UPDATED_BANK_ACCOUNT_NUMBER
        defaultDealerShouldBeFound("bankAccountNumber.notEquals=" + UPDATED_BANK_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDealersByBankAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankAccountNumber in DEFAULT_BANK_ACCOUNT_NUMBER or UPDATED_BANK_ACCOUNT_NUMBER
        defaultDealerShouldBeFound("bankAccountNumber.in=" + DEFAULT_BANK_ACCOUNT_NUMBER + "," + UPDATED_BANK_ACCOUNT_NUMBER);

        // Get all the dealerList where bankAccountNumber equals to UPDATED_BANK_ACCOUNT_NUMBER
        defaultDealerShouldNotBeFound("bankAccountNumber.in=" + UPDATED_BANK_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDealersByBankAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankAccountNumber is not null
        defaultDealerShouldBeFound("bankAccountNumber.specified=true");

        // Get all the dealerList where bankAccountNumber is null
        defaultDealerShouldNotBeFound("bankAccountNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByBankAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankAccountNumber contains DEFAULT_BANK_ACCOUNT_NUMBER
        defaultDealerShouldBeFound("bankAccountNumber.contains=" + DEFAULT_BANK_ACCOUNT_NUMBER);

        // Get all the dealerList where bankAccountNumber contains UPDATED_BANK_ACCOUNT_NUMBER
        defaultDealerShouldNotBeFound("bankAccountNumber.contains=" + UPDATED_BANK_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDealersByBankAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankAccountNumber does not contain DEFAULT_BANK_ACCOUNT_NUMBER
        defaultDealerShouldNotBeFound("bankAccountNumber.doesNotContain=" + DEFAULT_BANK_ACCOUNT_NUMBER);

        // Get all the dealerList where bankAccountNumber does not contain UPDATED_BANK_ACCOUNT_NUMBER
        defaultDealerShouldBeFound("bankAccountNumber.doesNotContain=" + UPDATED_BANK_ACCOUNT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllDealersByBankBranchIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankBranch equals to DEFAULT_BANK_BRANCH
        defaultDealerShouldBeFound("bankBranch.equals=" + DEFAULT_BANK_BRANCH);

        // Get all the dealerList where bankBranch equals to UPDATED_BANK_BRANCH
        defaultDealerShouldNotBeFound("bankBranch.equals=" + UPDATED_BANK_BRANCH);
    }

    @Test
    @Transactional
    public void getAllDealersByBankBranchIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankBranch not equals to DEFAULT_BANK_BRANCH
        defaultDealerShouldNotBeFound("bankBranch.notEquals=" + DEFAULT_BANK_BRANCH);

        // Get all the dealerList where bankBranch not equals to UPDATED_BANK_BRANCH
        defaultDealerShouldBeFound("bankBranch.notEquals=" + UPDATED_BANK_BRANCH);
    }

    @Test
    @Transactional
    public void getAllDealersByBankBranchIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankBranch in DEFAULT_BANK_BRANCH or UPDATED_BANK_BRANCH
        defaultDealerShouldBeFound("bankBranch.in=" + DEFAULT_BANK_BRANCH + "," + UPDATED_BANK_BRANCH);

        // Get all the dealerList where bankBranch equals to UPDATED_BANK_BRANCH
        defaultDealerShouldNotBeFound("bankBranch.in=" + UPDATED_BANK_BRANCH);
    }

    @Test
    @Transactional
    public void getAllDealersByBankBranchIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankBranch is not null
        defaultDealerShouldBeFound("bankBranch.specified=true");

        // Get all the dealerList where bankBranch is null
        defaultDealerShouldNotBeFound("bankBranch.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByBankBranchContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankBranch contains DEFAULT_BANK_BRANCH
        defaultDealerShouldBeFound("bankBranch.contains=" + DEFAULT_BANK_BRANCH);

        // Get all the dealerList where bankBranch contains UPDATED_BANK_BRANCH
        defaultDealerShouldNotBeFound("bankBranch.contains=" + UPDATED_BANK_BRANCH);
    }

    @Test
    @Transactional
    public void getAllDealersByBankBranchNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankBranch does not contain DEFAULT_BANK_BRANCH
        defaultDealerShouldNotBeFound("bankBranch.doesNotContain=" + DEFAULT_BANK_BRANCH);

        // Get all the dealerList where bankBranch does not contain UPDATED_BANK_BRANCH
        defaultDealerShouldBeFound("bankBranch.doesNotContain=" + UPDATED_BANK_BRANCH);
    }


    @Test
    @Transactional
    public void getAllDealersByBankSwiftCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankSwiftCode equals to DEFAULT_BANK_SWIFT_CODE
        defaultDealerShouldBeFound("bankSwiftCode.equals=" + DEFAULT_BANK_SWIFT_CODE);

        // Get all the dealerList where bankSwiftCode equals to UPDATED_BANK_SWIFT_CODE
        defaultDealerShouldNotBeFound("bankSwiftCode.equals=" + UPDATED_BANK_SWIFT_CODE);
    }

    @Test
    @Transactional
    public void getAllDealersByBankSwiftCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankSwiftCode not equals to DEFAULT_BANK_SWIFT_CODE
        defaultDealerShouldNotBeFound("bankSwiftCode.notEquals=" + DEFAULT_BANK_SWIFT_CODE);

        // Get all the dealerList where bankSwiftCode not equals to UPDATED_BANK_SWIFT_CODE
        defaultDealerShouldBeFound("bankSwiftCode.notEquals=" + UPDATED_BANK_SWIFT_CODE);
    }

    @Test
    @Transactional
    public void getAllDealersByBankSwiftCodeIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankSwiftCode in DEFAULT_BANK_SWIFT_CODE or UPDATED_BANK_SWIFT_CODE
        defaultDealerShouldBeFound("bankSwiftCode.in=" + DEFAULT_BANK_SWIFT_CODE + "," + UPDATED_BANK_SWIFT_CODE);

        // Get all the dealerList where bankSwiftCode equals to UPDATED_BANK_SWIFT_CODE
        defaultDealerShouldNotBeFound("bankSwiftCode.in=" + UPDATED_BANK_SWIFT_CODE);
    }

    @Test
    @Transactional
    public void getAllDealersByBankSwiftCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankSwiftCode is not null
        defaultDealerShouldBeFound("bankSwiftCode.specified=true");

        // Get all the dealerList where bankSwiftCode is null
        defaultDealerShouldNotBeFound("bankSwiftCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByBankSwiftCodeContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankSwiftCode contains DEFAULT_BANK_SWIFT_CODE
        defaultDealerShouldBeFound("bankSwiftCode.contains=" + DEFAULT_BANK_SWIFT_CODE);

        // Get all the dealerList where bankSwiftCode contains UPDATED_BANK_SWIFT_CODE
        defaultDealerShouldNotBeFound("bankSwiftCode.contains=" + UPDATED_BANK_SWIFT_CODE);
    }

    @Test
    @Transactional
    public void getAllDealersByBankSwiftCodeNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankSwiftCode does not contain DEFAULT_BANK_SWIFT_CODE
        defaultDealerShouldNotBeFound("bankSwiftCode.doesNotContain=" + DEFAULT_BANK_SWIFT_CODE);

        // Get all the dealerList where bankSwiftCode does not contain UPDATED_BANK_SWIFT_CODE
        defaultDealerShouldBeFound("bankSwiftCode.doesNotContain=" + UPDATED_BANK_SWIFT_CODE);
    }


    @Test
    @Transactional
    public void getAllDealersByBankPhysicalAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankPhysicalAddress equals to DEFAULT_BANK_PHYSICAL_ADDRESS
        defaultDealerShouldBeFound("bankPhysicalAddress.equals=" + DEFAULT_BANK_PHYSICAL_ADDRESS);

        // Get all the dealerList where bankPhysicalAddress equals to UPDATED_BANK_PHYSICAL_ADDRESS
        defaultDealerShouldNotBeFound("bankPhysicalAddress.equals=" + UPDATED_BANK_PHYSICAL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDealersByBankPhysicalAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankPhysicalAddress not equals to DEFAULT_BANK_PHYSICAL_ADDRESS
        defaultDealerShouldNotBeFound("bankPhysicalAddress.notEquals=" + DEFAULT_BANK_PHYSICAL_ADDRESS);

        // Get all the dealerList where bankPhysicalAddress not equals to UPDATED_BANK_PHYSICAL_ADDRESS
        defaultDealerShouldBeFound("bankPhysicalAddress.notEquals=" + UPDATED_BANK_PHYSICAL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDealersByBankPhysicalAddressIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankPhysicalAddress in DEFAULT_BANK_PHYSICAL_ADDRESS or UPDATED_BANK_PHYSICAL_ADDRESS
        defaultDealerShouldBeFound("bankPhysicalAddress.in=" + DEFAULT_BANK_PHYSICAL_ADDRESS + "," + UPDATED_BANK_PHYSICAL_ADDRESS);

        // Get all the dealerList where bankPhysicalAddress equals to UPDATED_BANK_PHYSICAL_ADDRESS
        defaultDealerShouldNotBeFound("bankPhysicalAddress.in=" + UPDATED_BANK_PHYSICAL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDealersByBankPhysicalAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankPhysicalAddress is not null
        defaultDealerShouldBeFound("bankPhysicalAddress.specified=true");

        // Get all the dealerList where bankPhysicalAddress is null
        defaultDealerShouldNotBeFound("bankPhysicalAddress.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByBankPhysicalAddressContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankPhysicalAddress contains DEFAULT_BANK_PHYSICAL_ADDRESS
        defaultDealerShouldBeFound("bankPhysicalAddress.contains=" + DEFAULT_BANK_PHYSICAL_ADDRESS);

        // Get all the dealerList where bankPhysicalAddress contains UPDATED_BANK_PHYSICAL_ADDRESS
        defaultDealerShouldNotBeFound("bankPhysicalAddress.contains=" + UPDATED_BANK_PHYSICAL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDealersByBankPhysicalAddressNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankPhysicalAddress does not contain DEFAULT_BANK_PHYSICAL_ADDRESS
        defaultDealerShouldNotBeFound("bankPhysicalAddress.doesNotContain=" + DEFAULT_BANK_PHYSICAL_ADDRESS);

        // Get all the dealerList where bankPhysicalAddress does not contain UPDATED_BANK_PHYSICAL_ADDRESS
        defaultDealerShouldBeFound("bankPhysicalAddress.doesNotContain=" + UPDATED_BANK_PHYSICAL_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllDealersByDomicileIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where domicile equals to DEFAULT_DOMICILE
        defaultDealerShouldBeFound("domicile.equals=" + DEFAULT_DOMICILE);

        // Get all the dealerList where domicile equals to UPDATED_DOMICILE
        defaultDealerShouldNotBeFound("domicile.equals=" + UPDATED_DOMICILE);
    }

    @Test
    @Transactional
    public void getAllDealersByDomicileIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where domicile not equals to DEFAULT_DOMICILE
        defaultDealerShouldNotBeFound("domicile.notEquals=" + DEFAULT_DOMICILE);

        // Get all the dealerList where domicile not equals to UPDATED_DOMICILE
        defaultDealerShouldBeFound("domicile.notEquals=" + UPDATED_DOMICILE);
    }

    @Test
    @Transactional
    public void getAllDealersByDomicileIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where domicile in DEFAULT_DOMICILE or UPDATED_DOMICILE
        defaultDealerShouldBeFound("domicile.in=" + DEFAULT_DOMICILE + "," + UPDATED_DOMICILE);

        // Get all the dealerList where domicile equals to UPDATED_DOMICILE
        defaultDealerShouldNotBeFound("domicile.in=" + UPDATED_DOMICILE);
    }

    @Test
    @Transactional
    public void getAllDealersByDomicileIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where domicile is not null
        defaultDealerShouldBeFound("domicile.specified=true");

        // Get all the dealerList where domicile is null
        defaultDealerShouldNotBeFound("domicile.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByDomicileContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where domicile contains DEFAULT_DOMICILE
        defaultDealerShouldBeFound("domicile.contains=" + DEFAULT_DOMICILE);

        // Get all the dealerList where domicile contains UPDATED_DOMICILE
        defaultDealerShouldNotBeFound("domicile.contains=" + UPDATED_DOMICILE);
    }

    @Test
    @Transactional
    public void getAllDealersByDomicileNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where domicile does not contain DEFAULT_DOMICILE
        defaultDealerShouldNotBeFound("domicile.doesNotContain=" + DEFAULT_DOMICILE);

        // Get all the dealerList where domicile does not contain UPDATED_DOMICILE
        defaultDealerShouldBeFound("domicile.doesNotContain=" + UPDATED_DOMICILE);
    }


    @Test
    @Transactional
    public void getAllDealersByTaxAuthorityRefIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxAuthorityRef equals to DEFAULT_TAX_AUTHORITY_REF
        defaultDealerShouldBeFound("taxAuthorityRef.equals=" + DEFAULT_TAX_AUTHORITY_REF);

        // Get all the dealerList where taxAuthorityRef equals to UPDATED_TAX_AUTHORITY_REF
        defaultDealerShouldNotBeFound("taxAuthorityRef.equals=" + UPDATED_TAX_AUTHORITY_REF);
    }

    @Test
    @Transactional
    public void getAllDealersByTaxAuthorityRefIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxAuthorityRef not equals to DEFAULT_TAX_AUTHORITY_REF
        defaultDealerShouldNotBeFound("taxAuthorityRef.notEquals=" + DEFAULT_TAX_AUTHORITY_REF);

        // Get all the dealerList where taxAuthorityRef not equals to UPDATED_TAX_AUTHORITY_REF
        defaultDealerShouldBeFound("taxAuthorityRef.notEquals=" + UPDATED_TAX_AUTHORITY_REF);
    }

    @Test
    @Transactional
    public void getAllDealersByTaxAuthorityRefIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxAuthorityRef in DEFAULT_TAX_AUTHORITY_REF or UPDATED_TAX_AUTHORITY_REF
        defaultDealerShouldBeFound("taxAuthorityRef.in=" + DEFAULT_TAX_AUTHORITY_REF + "," + UPDATED_TAX_AUTHORITY_REF);

        // Get all the dealerList where taxAuthorityRef equals to UPDATED_TAX_AUTHORITY_REF
        defaultDealerShouldNotBeFound("taxAuthorityRef.in=" + UPDATED_TAX_AUTHORITY_REF);
    }

    @Test
    @Transactional
    public void getAllDealersByTaxAuthorityRefIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxAuthorityRef is not null
        defaultDealerShouldBeFound("taxAuthorityRef.specified=true");

        // Get all the dealerList where taxAuthorityRef is null
        defaultDealerShouldNotBeFound("taxAuthorityRef.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByTaxAuthorityRefContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxAuthorityRef contains DEFAULT_TAX_AUTHORITY_REF
        defaultDealerShouldBeFound("taxAuthorityRef.contains=" + DEFAULT_TAX_AUTHORITY_REF);

        // Get all the dealerList where taxAuthorityRef contains UPDATED_TAX_AUTHORITY_REF
        defaultDealerShouldNotBeFound("taxAuthorityRef.contains=" + UPDATED_TAX_AUTHORITY_REF);
    }

    @Test
    @Transactional
    public void getAllDealersByTaxAuthorityRefNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxAuthorityRef does not contain DEFAULT_TAX_AUTHORITY_REF
        defaultDealerShouldNotBeFound("taxAuthorityRef.doesNotContain=" + DEFAULT_TAX_AUTHORITY_REF);

        // Get all the dealerList where taxAuthorityRef does not contain UPDATED_TAX_AUTHORITY_REF
        defaultDealerShouldBeFound("taxAuthorityRef.doesNotContain=" + UPDATED_TAX_AUTHORITY_REF);
    }


    @Test
    @Transactional
    public void getAllDealersByFixedAssetInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);
        FixedAssetInvoice fixedAssetInvoice = FixedAssetInvoiceResourceIT.createEntity(em);
        em.persist(fixedAssetInvoice);
        em.flush();
        dealer.addFixedAssetInvoice(fixedAssetInvoice);
        dealerRepository.saveAndFlush(dealer);
        Long fixedAssetInvoiceId = fixedAssetInvoice.getId();

        // Get all the dealerList where fixedAssetInvoice equals to fixedAssetInvoiceId
        defaultDealerShouldBeFound("fixedAssetInvoiceId.equals=" + fixedAssetInvoiceId);

        // Get all the dealerList where fixedAssetInvoice equals to fixedAssetInvoiceId + 1
        defaultDealerShouldNotBeFound("fixedAssetInvoiceId.equals=" + (fixedAssetInvoiceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDealerShouldBeFound(String filter) throws Exception {
        restDealerMockMvc.perform(get("/api/dealers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealer.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].dealerAddress").value(hasItem(DEFAULT_DEALER_ADDRESS)))
            .andExpect(jsonPath("$.[*].dealerPhoneNumber").value(hasItem(DEFAULT_DEALER_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].dealerEmail").value(hasItem(DEFAULT_DEALER_EMAIL)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].bankAccountNumber").value(hasItem(DEFAULT_BANK_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].bankBranch").value(hasItem(DEFAULT_BANK_BRANCH)))
            .andExpect(jsonPath("$.[*].bankSwiftCode").value(hasItem(DEFAULT_BANK_SWIFT_CODE)))
            .andExpect(jsonPath("$.[*].bankPhysicalAddress").value(hasItem(DEFAULT_BANK_PHYSICAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].domicile").value(hasItem(DEFAULT_DOMICILE)))
            .andExpect(jsonPath("$.[*].taxAuthorityRef").value(hasItem(DEFAULT_TAX_AUTHORITY_REF)));

        // Check, that the count call also returns 1
        restDealerMockMvc.perform(get("/api/dealers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDealerShouldNotBeFound(String filter) throws Exception {
        restDealerMockMvc.perform(get("/api/dealers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDealerMockMvc.perform(get("/api/dealers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDealer() throws Exception {
        // Get the dealer
        restDealerMockMvc.perform(get("/api/dealers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDealer() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        int databaseSizeBeforeUpdate = dealerRepository.findAll().size();

        // Update the dealer
        Dealer updatedDealer = dealerRepository.findById(dealer.getId()).get();
        // Disconnect from session so that the updates on updatedDealer are not directly saved in db
        em.detach(updatedDealer);
        updatedDealer
            .title(UPDATED_TITLE)
            .dealerName(UPDATED_DEALER_NAME)
            .dealerAddress(UPDATED_DEALER_ADDRESS)
            .dealerPhoneNumber(UPDATED_DEALER_PHONE_NUMBER)
            .dealerEmail(UPDATED_DEALER_EMAIL)
            .bankName(UPDATED_BANK_NAME)
            .bankAccountNumber(UPDATED_BANK_ACCOUNT_NUMBER)
            .bankBranch(UPDATED_BANK_BRANCH)
            .bankSwiftCode(UPDATED_BANK_SWIFT_CODE)
            .bankPhysicalAddress(UPDATED_BANK_PHYSICAL_ADDRESS)
            .domicile(UPDATED_DOMICILE)
            .taxAuthorityRef(UPDATED_TAX_AUTHORITY_REF);
        DealerDTO dealerDTO = dealerMapper.toDto(updatedDealer);

        restDealerMockMvc.perform(put("/api/dealers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dealerDTO)))
            .andExpect(status().isOk());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeUpdate);
        Dealer testDealer = dealerList.get(dealerList.size() - 1);
        assertThat(testDealer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDealer.getDealerName()).isEqualTo(UPDATED_DEALER_NAME);
        assertThat(testDealer.getDealerAddress()).isEqualTo(UPDATED_DEALER_ADDRESS);
        assertThat(testDealer.getDealerPhoneNumber()).isEqualTo(UPDATED_DEALER_PHONE_NUMBER);
        assertThat(testDealer.getDealerEmail()).isEqualTo(UPDATED_DEALER_EMAIL);
        assertThat(testDealer.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testDealer.getBankAccountNumber()).isEqualTo(UPDATED_BANK_ACCOUNT_NUMBER);
        assertThat(testDealer.getBankBranch()).isEqualTo(UPDATED_BANK_BRANCH);
        assertThat(testDealer.getBankSwiftCode()).isEqualTo(UPDATED_BANK_SWIFT_CODE);
        assertThat(testDealer.getBankPhysicalAddress()).isEqualTo(UPDATED_BANK_PHYSICAL_ADDRESS);
        assertThat(testDealer.getDomicile()).isEqualTo(UPDATED_DOMICILE);
        assertThat(testDealer.getTaxAuthorityRef()).isEqualTo(UPDATED_TAX_AUTHORITY_REF);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(1)).save(testDealer);
    }

    @Test
    @Transactional
    public void updateNonExistingDealer() throws Exception {
        int databaseSizeBeforeUpdate = dealerRepository.findAll().size();

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealerMockMvc.perform(put("/api/dealers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dealerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(0)).save(dealer);
    }

    @Test
    @Transactional
    public void deleteDealer() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        int databaseSizeBeforeDelete = dealerRepository.findAll().size();

        // Delete the dealer
        restDealerMockMvc.perform(delete("/api/dealers/{id}", dealer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(1)).deleteById(dealer.getId());
    }

    @Test
    @Transactional
    public void searchDealer() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);
        when(mockDealerSearchRepository.search(queryStringQuery("id:" + dealer.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(dealer), PageRequest.of(0, 1), 1));
        // Search the dealer
        restDealerMockMvc.perform(get("/api/_search/dealers?query=id:" + dealer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealer.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].dealerAddress").value(hasItem(DEFAULT_DEALER_ADDRESS)))
            .andExpect(jsonPath("$.[*].dealerPhoneNumber").value(hasItem(DEFAULT_DEALER_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].dealerEmail").value(hasItem(DEFAULT_DEALER_EMAIL)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].bankAccountNumber").value(hasItem(DEFAULT_BANK_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].bankBranch").value(hasItem(DEFAULT_BANK_BRANCH)))
            .andExpect(jsonPath("$.[*].bankSwiftCode").value(hasItem(DEFAULT_BANK_SWIFT_CODE)))
            .andExpect(jsonPath("$.[*].bankPhysicalAddress").value(hasItem(DEFAULT_BANK_PHYSICAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].domicile").value(hasItem(DEFAULT_DOMICILE)))
            .andExpect(jsonPath("$.[*].taxAuthorityRef").value(hasItem(DEFAULT_TAX_AUTHORITY_REF)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dealer.class);
        Dealer dealer1 = new Dealer();
        dealer1.setId(1L);
        Dealer dealer2 = new Dealer();
        dealer2.setId(dealer1.getId());
        assertThat(dealer1).isEqualTo(dealer2);
        dealer2.setId(2L);
        assertThat(dealer1).isNotEqualTo(dealer2);
        dealer1.setId(null);
        assertThat(dealer1).isNotEqualTo(dealer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DealerDTO.class);
        DealerDTO dealerDTO1 = new DealerDTO();
        dealerDTO1.setId(1L);
        DealerDTO dealerDTO2 = new DealerDTO();
        assertThat(dealerDTO1).isNotEqualTo(dealerDTO2);
        dealerDTO2.setId(dealerDTO1.getId());
        assertThat(dealerDTO1).isEqualTo(dealerDTO2);
        dealerDTO2.setId(2L);
        assertThat(dealerDTO1).isNotEqualTo(dealerDTO2);
        dealerDTO1.setId(null);
        assertThat(dealerDTO1).isNotEqualTo(dealerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(dealerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(dealerMapper.fromId(null)).isNull();
    }
}
