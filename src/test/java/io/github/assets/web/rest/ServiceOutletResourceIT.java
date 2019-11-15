package io.github.assets.web.rest;

import io.github.assets.FixedAssetServiceApp;
import io.github.assets.config.SecurityBeanOverrideConfiguration;
import io.github.assets.domain.ServiceOutlet;
import io.github.assets.repository.ServiceOutletRepository;
import io.github.assets.service.ServiceOutletService;
import io.github.assets.service.dto.ServiceOutletDTO;
import io.github.assets.service.mapper.ServiceOutletMapper;
import io.github.assets.web.rest.errors.ExceptionTranslator;
import io.github.assets.service.dto.ServiceOutletCriteria;
import io.github.assets.service.ServiceOutletQueryService;

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

/**
 * Integration tests for the {@link ServiceOutletResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, FixedAssetServiceApp.class})
public class ServiceOutletResourceIT {

    private static final String DEFAULT_SERVICE_OUTLET_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_OUTLET_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_OUTLET_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_OUTLET_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    @Autowired
    private ServiceOutletRepository serviceOutletRepository;

    @Autowired
    private ServiceOutletMapper serviceOutletMapper;

    @Autowired
    private ServiceOutletService serviceOutletService;

    @Autowired
    private ServiceOutletQueryService serviceOutletQueryService;

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

    private MockMvc restServiceOutletMockMvc;

    private ServiceOutlet serviceOutlet;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceOutletResource serviceOutletResource = new ServiceOutletResource(serviceOutletService, serviceOutletQueryService);
        this.restServiceOutletMockMvc = MockMvcBuilders.standaloneSetup(serviceOutletResource)
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
    public static ServiceOutlet createEntity(EntityManager em) {
        ServiceOutlet serviceOutlet = new ServiceOutlet()
            .serviceOutletCode(DEFAULT_SERVICE_OUTLET_CODE)
            .serviceOutletDesignation(DEFAULT_SERVICE_OUTLET_DESIGNATION)
            .description(DEFAULT_DESCRIPTION)
            .location(DEFAULT_LOCATION);
        return serviceOutlet;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceOutlet createUpdatedEntity(EntityManager em) {
        ServiceOutlet serviceOutlet = new ServiceOutlet()
            .serviceOutletCode(UPDATED_SERVICE_OUTLET_CODE)
            .serviceOutletDesignation(UPDATED_SERVICE_OUTLET_DESIGNATION)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION);
        return serviceOutlet;
    }

    @BeforeEach
    public void initTest() {
        serviceOutlet = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceOutlet() throws Exception {
        int databaseSizeBeforeCreate = serviceOutletRepository.findAll().size();

        // Create the ServiceOutlet
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);
        restServiceOutletMockMvc.perform(post("/api/service-outlets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO)))
            .andExpect(status().isCreated());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceOutlet testServiceOutlet = serviceOutletList.get(serviceOutletList.size() - 1);
        assertThat(testServiceOutlet.getServiceOutletCode()).isEqualTo(DEFAULT_SERVICE_OUTLET_CODE);
        assertThat(testServiceOutlet.getServiceOutletDesignation()).isEqualTo(DEFAULT_SERVICE_OUTLET_DESIGNATION);
        assertThat(testServiceOutlet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testServiceOutlet.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    public void createServiceOutletWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceOutletRepository.findAll().size();

        // Create the ServiceOutlet with an existing ID
        serviceOutlet.setId(1L);
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceOutletMockMvc.perform(post("/api/service-outlets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkServiceOutletCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceOutletRepository.findAll().size();
        // set the field null
        serviceOutlet.setServiceOutletCode(null);

        // Create the ServiceOutlet, which fails.
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);

        restServiceOutletMockMvc.perform(post("/api/service-outlets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO)))
            .andExpect(status().isBadRequest());

        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServiceOutletDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceOutletRepository.findAll().size();
        // set the field null
        serviceOutlet.setServiceOutletDesignation(null);

        // Create the ServiceOutlet, which fails.
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);

        restServiceOutletMockMvc.perform(post("/api/service-outlets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO)))
            .andExpect(status().isBadRequest());

        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServiceOutlets() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList
        restServiceOutletMockMvc.perform(get("/api/service-outlets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOutlet.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].serviceOutletDesignation").value(hasItem(DEFAULT_SERVICE_OUTLET_DESIGNATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }
    
    @Test
    @Transactional
    public void getServiceOutlet() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get the serviceOutlet
        restServiceOutletMockMvc.perform(get("/api/service-outlets/{id}", serviceOutlet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceOutlet.getId().intValue()))
            .andExpect(jsonPath("$.serviceOutletCode").value(DEFAULT_SERVICE_OUTLET_CODE))
            .andExpect(jsonPath("$.serviceOutletDesignation").value(DEFAULT_SERVICE_OUTLET_DESIGNATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }


    @Test
    @Transactional
    public void getServiceOutletsByIdFiltering() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        Long id = serviceOutlet.getId();

        defaultServiceOutletShouldBeFound("id.equals=" + id);
        defaultServiceOutletShouldNotBeFound("id.notEquals=" + id);

        defaultServiceOutletShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultServiceOutletShouldNotBeFound("id.greaterThan=" + id);

        defaultServiceOutletShouldBeFound("id.lessThanOrEqual=" + id);
        defaultServiceOutletShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutletCode equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultServiceOutletShouldBeFound("serviceOutletCode.equals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the serviceOutletList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultServiceOutletShouldNotBeFound("serviceOutletCode.equals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutletCode not equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultServiceOutletShouldNotBeFound("serviceOutletCode.notEquals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the serviceOutletList where serviceOutletCode not equals to UPDATED_SERVICE_OUTLET_CODE
        defaultServiceOutletShouldBeFound("serviceOutletCode.notEquals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletCodeIsInShouldWork() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutletCode in DEFAULT_SERVICE_OUTLET_CODE or UPDATED_SERVICE_OUTLET_CODE
        defaultServiceOutletShouldBeFound("serviceOutletCode.in=" + DEFAULT_SERVICE_OUTLET_CODE + "," + UPDATED_SERVICE_OUTLET_CODE);

        // Get all the serviceOutletList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultServiceOutletShouldNotBeFound("serviceOutletCode.in=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutletCode is not null
        defaultServiceOutletShouldBeFound("serviceOutletCode.specified=true");

        // Get all the serviceOutletList where serviceOutletCode is null
        defaultServiceOutletShouldNotBeFound("serviceOutletCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletCodeContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutletCode contains DEFAULT_SERVICE_OUTLET_CODE
        defaultServiceOutletShouldBeFound("serviceOutletCode.contains=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the serviceOutletList where serviceOutletCode contains UPDATED_SERVICE_OUTLET_CODE
        defaultServiceOutletShouldNotBeFound("serviceOutletCode.contains=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletCodeNotContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutletCode does not contain DEFAULT_SERVICE_OUTLET_CODE
        defaultServiceOutletShouldNotBeFound("serviceOutletCode.doesNotContain=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the serviceOutletList where serviceOutletCode does not contain UPDATED_SERVICE_OUTLET_CODE
        defaultServiceOutletShouldBeFound("serviceOutletCode.doesNotContain=" + UPDATED_SERVICE_OUTLET_CODE);
    }


    @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletDesignationIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutletDesignation equals to DEFAULT_SERVICE_OUTLET_DESIGNATION
        defaultServiceOutletShouldBeFound("serviceOutletDesignation.equals=" + DEFAULT_SERVICE_OUTLET_DESIGNATION);

        // Get all the serviceOutletList where serviceOutletDesignation equals to UPDATED_SERVICE_OUTLET_DESIGNATION
        defaultServiceOutletShouldNotBeFound("serviceOutletDesignation.equals=" + UPDATED_SERVICE_OUTLET_DESIGNATION);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletDesignationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutletDesignation not equals to DEFAULT_SERVICE_OUTLET_DESIGNATION
        defaultServiceOutletShouldNotBeFound("serviceOutletDesignation.notEquals=" + DEFAULT_SERVICE_OUTLET_DESIGNATION);

        // Get all the serviceOutletList where serviceOutletDesignation not equals to UPDATED_SERVICE_OUTLET_DESIGNATION
        defaultServiceOutletShouldBeFound("serviceOutletDesignation.notEquals=" + UPDATED_SERVICE_OUTLET_DESIGNATION);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletDesignationIsInShouldWork() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutletDesignation in DEFAULT_SERVICE_OUTLET_DESIGNATION or UPDATED_SERVICE_OUTLET_DESIGNATION
        defaultServiceOutletShouldBeFound("serviceOutletDesignation.in=" + DEFAULT_SERVICE_OUTLET_DESIGNATION + "," + UPDATED_SERVICE_OUTLET_DESIGNATION);

        // Get all the serviceOutletList where serviceOutletDesignation equals to UPDATED_SERVICE_OUTLET_DESIGNATION
        defaultServiceOutletShouldNotBeFound("serviceOutletDesignation.in=" + UPDATED_SERVICE_OUTLET_DESIGNATION);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletDesignationIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutletDesignation is not null
        defaultServiceOutletShouldBeFound("serviceOutletDesignation.specified=true");

        // Get all the serviceOutletList where serviceOutletDesignation is null
        defaultServiceOutletShouldNotBeFound("serviceOutletDesignation.specified=false");
    }
                @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletDesignationContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutletDesignation contains DEFAULT_SERVICE_OUTLET_DESIGNATION
        defaultServiceOutletShouldBeFound("serviceOutletDesignation.contains=" + DEFAULT_SERVICE_OUTLET_DESIGNATION);

        // Get all the serviceOutletList where serviceOutletDesignation contains UPDATED_SERVICE_OUTLET_DESIGNATION
        defaultServiceOutletShouldNotBeFound("serviceOutletDesignation.contains=" + UPDATED_SERVICE_OUTLET_DESIGNATION);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByServiceOutletDesignationNotContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where serviceOutletDesignation does not contain DEFAULT_SERVICE_OUTLET_DESIGNATION
        defaultServiceOutletShouldNotBeFound("serviceOutletDesignation.doesNotContain=" + DEFAULT_SERVICE_OUTLET_DESIGNATION);

        // Get all the serviceOutletList where serviceOutletDesignation does not contain UPDATED_SERVICE_OUTLET_DESIGNATION
        defaultServiceOutletShouldBeFound("serviceOutletDesignation.doesNotContain=" + UPDATED_SERVICE_OUTLET_DESIGNATION);
    }


    @Test
    @Transactional
    public void getAllServiceOutletsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where description equals to DEFAULT_DESCRIPTION
        defaultServiceOutletShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the serviceOutletList where description equals to UPDATED_DESCRIPTION
        defaultServiceOutletShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where description not equals to DEFAULT_DESCRIPTION
        defaultServiceOutletShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the serviceOutletList where description not equals to UPDATED_DESCRIPTION
        defaultServiceOutletShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultServiceOutletShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the serviceOutletList where description equals to UPDATED_DESCRIPTION
        defaultServiceOutletShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where description is not null
        defaultServiceOutletShouldBeFound("description.specified=true");

        // Get all the serviceOutletList where description is null
        defaultServiceOutletShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllServiceOutletsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where description contains DEFAULT_DESCRIPTION
        defaultServiceOutletShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the serviceOutletList where description contains UPDATED_DESCRIPTION
        defaultServiceOutletShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where description does not contain DEFAULT_DESCRIPTION
        defaultServiceOutletShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the serviceOutletList where description does not contain UPDATED_DESCRIPTION
        defaultServiceOutletShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllServiceOutletsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where location equals to DEFAULT_LOCATION
        defaultServiceOutletShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the serviceOutletList where location equals to UPDATED_LOCATION
        defaultServiceOutletShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where location not equals to DEFAULT_LOCATION
        defaultServiceOutletShouldNotBeFound("location.notEquals=" + DEFAULT_LOCATION);

        // Get all the serviceOutletList where location not equals to UPDATED_LOCATION
        defaultServiceOutletShouldBeFound("location.notEquals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultServiceOutletShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the serviceOutletList where location equals to UPDATED_LOCATION
        defaultServiceOutletShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where location is not null
        defaultServiceOutletShouldBeFound("location.specified=true");

        // Get all the serviceOutletList where location is null
        defaultServiceOutletShouldNotBeFound("location.specified=false");
    }
                @Test
    @Transactional
    public void getAllServiceOutletsByLocationContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where location contains DEFAULT_LOCATION
        defaultServiceOutletShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the serviceOutletList where location contains UPDATED_LOCATION
        defaultServiceOutletShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllServiceOutletsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        // Get all the serviceOutletList where location does not contain DEFAULT_LOCATION
        defaultServiceOutletShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the serviceOutletList where location does not contain UPDATED_LOCATION
        defaultServiceOutletShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServiceOutletShouldBeFound(String filter) throws Exception {
        restServiceOutletMockMvc.perform(get("/api/service-outlets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOutlet.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].serviceOutletDesignation").value(hasItem(DEFAULT_SERVICE_OUTLET_DESIGNATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));

        // Check, that the count call also returns 1
        restServiceOutletMockMvc.perform(get("/api/service-outlets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServiceOutletShouldNotBeFound(String filter) throws Exception {
        restServiceOutletMockMvc.perform(get("/api/service-outlets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServiceOutletMockMvc.perform(get("/api/service-outlets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingServiceOutlet() throws Exception {
        // Get the serviceOutlet
        restServiceOutletMockMvc.perform(get("/api/service-outlets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceOutlet() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        int databaseSizeBeforeUpdate = serviceOutletRepository.findAll().size();

        // Update the serviceOutlet
        ServiceOutlet updatedServiceOutlet = serviceOutletRepository.findById(serviceOutlet.getId()).get();
        // Disconnect from session so that the updates on updatedServiceOutlet are not directly saved in db
        em.detach(updatedServiceOutlet);
        updatedServiceOutlet
            .serviceOutletCode(UPDATED_SERVICE_OUTLET_CODE)
            .serviceOutletDesignation(UPDATED_SERVICE_OUTLET_DESIGNATION)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION);
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(updatedServiceOutlet);

        restServiceOutletMockMvc.perform(put("/api/service-outlets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO)))
            .andExpect(status().isOk());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeUpdate);
        ServiceOutlet testServiceOutlet = serviceOutletList.get(serviceOutletList.size() - 1);
        assertThat(testServiceOutlet.getServiceOutletCode()).isEqualTo(UPDATED_SERVICE_OUTLET_CODE);
        assertThat(testServiceOutlet.getServiceOutletDesignation()).isEqualTo(UPDATED_SERVICE_OUTLET_DESIGNATION);
        assertThat(testServiceOutlet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testServiceOutlet.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceOutlet() throws Exception {
        int databaseSizeBeforeUpdate = serviceOutletRepository.findAll().size();

        // Create the ServiceOutlet
        ServiceOutletDTO serviceOutletDTO = serviceOutletMapper.toDto(serviceOutlet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceOutletMockMvc.perform(put("/api/service-outlets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceOutletDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceOutlet in the database
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServiceOutlet() throws Exception {
        // Initialize the database
        serviceOutletRepository.saveAndFlush(serviceOutlet);

        int databaseSizeBeforeDelete = serviceOutletRepository.findAll().size();

        // Delete the serviceOutlet
        restServiceOutletMockMvc.perform(delete("/api/service-outlets/{id}", serviceOutlet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceOutlet> serviceOutletList = serviceOutletRepository.findAll();
        assertThat(serviceOutletList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
