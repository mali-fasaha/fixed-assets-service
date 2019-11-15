package io.github.assets.web.rest;

import io.github.assets.FixedAssetServiceApp;
import io.github.assets.config.SecurityBeanOverrideConfiguration;
import io.github.assets.domain.FixedAssetCategory;
import io.github.assets.repository.FixedAssetCategoryRepository;
import io.github.assets.service.FixedAssetCategoryService;
import io.github.assets.service.dto.FixedAssetCategoryDTO;
import io.github.assets.service.mapper.FixedAssetCategoryMapper;
import io.github.assets.web.rest.errors.ExceptionTranslator;
import io.github.assets.service.dto.FixedAssetCategoryCriteria;
import io.github.assets.service.FixedAssetCategoryQueryService;

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
 * Integration tests for the {@link FixedAssetCategoryResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, FixedAssetServiceApp.class})
public class FixedAssetCategoryResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_ASSET_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_ASSET_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_DEPRECIATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_DEPRECIATION_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_DEPRECIATION_REGIME_ID = 1L;
    private static final Long UPDATED_DEPRECIATION_REGIME_ID = 2L;
    private static final Long SMALLER_DEPRECIATION_REGIME_ID = 1L - 1L;

    @Autowired
    private FixedAssetCategoryRepository fixedAssetCategoryRepository;

    @Autowired
    private FixedAssetCategoryMapper fixedAssetCategoryMapper;

    @Autowired
    private FixedAssetCategoryService fixedAssetCategoryService;

    @Autowired
    private FixedAssetCategoryQueryService fixedAssetCategoryQueryService;

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

    private MockMvc restFixedAssetCategoryMockMvc;

    private FixedAssetCategory fixedAssetCategory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FixedAssetCategoryResource fixedAssetCategoryResource = new FixedAssetCategoryResource(fixedAssetCategoryService, fixedAssetCategoryQueryService);
        this.restFixedAssetCategoryMockMvc = MockMvcBuilders.standaloneSetup(fixedAssetCategoryResource)
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
    public static FixedAssetCategory createEntity(EntityManager em) {
        FixedAssetCategory fixedAssetCategory = new FixedAssetCategory()
            .categoryName(DEFAULT_CATEGORY_NAME)
            .categoryDescription(DEFAULT_CATEGORY_DESCRIPTION)
            .categoryAssetCode(DEFAULT_CATEGORY_ASSET_CODE)
            .categoryDepreciationCode(DEFAULT_CATEGORY_DEPRECIATION_CODE)
            .depreciationRegimeId(DEFAULT_DEPRECIATION_REGIME_ID);
        return fixedAssetCategory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FixedAssetCategory createUpdatedEntity(EntityManager em) {
        FixedAssetCategory fixedAssetCategory = new FixedAssetCategory()
            .categoryName(UPDATED_CATEGORY_NAME)
            .categoryDescription(UPDATED_CATEGORY_DESCRIPTION)
            .categoryAssetCode(UPDATED_CATEGORY_ASSET_CODE)
            .categoryDepreciationCode(UPDATED_CATEGORY_DEPRECIATION_CODE)
            .depreciationRegimeId(UPDATED_DEPRECIATION_REGIME_ID);
        return fixedAssetCategory;
    }

    @BeforeEach
    public void initTest() {
        fixedAssetCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createFixedAssetCategory() throws Exception {
        int databaseSizeBeforeCreate = fixedAssetCategoryRepository.findAll().size();

        // Create the FixedAssetCategory
        FixedAssetCategoryDTO fixedAssetCategoryDTO = fixedAssetCategoryMapper.toDto(fixedAssetCategory);
        restFixedAssetCategoryMockMvc.perform(post("/api/fixed-asset-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fixedAssetCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the FixedAssetCategory in the database
        List<FixedAssetCategory> fixedAssetCategoryList = fixedAssetCategoryRepository.findAll();
        assertThat(fixedAssetCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        FixedAssetCategory testFixedAssetCategory = fixedAssetCategoryList.get(fixedAssetCategoryList.size() - 1);
        assertThat(testFixedAssetCategory.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(testFixedAssetCategory.getCategoryDescription()).isEqualTo(DEFAULT_CATEGORY_DESCRIPTION);
        assertThat(testFixedAssetCategory.getCategoryAssetCode()).isEqualTo(DEFAULT_CATEGORY_ASSET_CODE);
        assertThat(testFixedAssetCategory.getCategoryDepreciationCode()).isEqualTo(DEFAULT_CATEGORY_DEPRECIATION_CODE);
        assertThat(testFixedAssetCategory.getDepreciationRegimeId()).isEqualTo(DEFAULT_DEPRECIATION_REGIME_ID);
    }

    @Test
    @Transactional
    public void createFixedAssetCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fixedAssetCategoryRepository.findAll().size();

        // Create the FixedAssetCategory with an existing ID
        fixedAssetCategory.setId(1L);
        FixedAssetCategoryDTO fixedAssetCategoryDTO = fixedAssetCategoryMapper.toDto(fixedAssetCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFixedAssetCategoryMockMvc.perform(post("/api/fixed-asset-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fixedAssetCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FixedAssetCategory in the database
        List<FixedAssetCategory> fixedAssetCategoryList = fixedAssetCategoryRepository.findAll();
        assertThat(fixedAssetCategoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCategoryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fixedAssetCategoryRepository.findAll().size();
        // set the field null
        fixedAssetCategory.setCategoryName(null);

        // Create the FixedAssetCategory, which fails.
        FixedAssetCategoryDTO fixedAssetCategoryDTO = fixedAssetCategoryMapper.toDto(fixedAssetCategory);

        restFixedAssetCategoryMockMvc.perform(post("/api/fixed-asset-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fixedAssetCategoryDTO)))
            .andExpect(status().isBadRequest());

        List<FixedAssetCategory> fixedAssetCategoryList = fixedAssetCategoryRepository.findAll();
        assertThat(fixedAssetCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCategoryAssetCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fixedAssetCategoryRepository.findAll().size();
        // set the field null
        fixedAssetCategory.setCategoryAssetCode(null);

        // Create the FixedAssetCategory, which fails.
        FixedAssetCategoryDTO fixedAssetCategoryDTO = fixedAssetCategoryMapper.toDto(fixedAssetCategory);

        restFixedAssetCategoryMockMvc.perform(post("/api/fixed-asset-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fixedAssetCategoryDTO)))
            .andExpect(status().isBadRequest());

        List<FixedAssetCategory> fixedAssetCategoryList = fixedAssetCategoryRepository.findAll();
        assertThat(fixedAssetCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCategoryDepreciationCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fixedAssetCategoryRepository.findAll().size();
        // set the field null
        fixedAssetCategory.setCategoryDepreciationCode(null);

        // Create the FixedAssetCategory, which fails.
        FixedAssetCategoryDTO fixedAssetCategoryDTO = fixedAssetCategoryMapper.toDto(fixedAssetCategory);

        restFixedAssetCategoryMockMvc.perform(post("/api/fixed-asset-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fixedAssetCategoryDTO)))
            .andExpect(status().isBadRequest());

        List<FixedAssetCategory> fixedAssetCategoryList = fixedAssetCategoryRepository.findAll();
        assertThat(fixedAssetCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategories() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList
        restFixedAssetCategoryMockMvc.perform(get("/api/fixed-asset-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixedAssetCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].categoryDescription").value(hasItem(DEFAULT_CATEGORY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].categoryAssetCode").value(hasItem(DEFAULT_CATEGORY_ASSET_CODE)))
            .andExpect(jsonPath("$.[*].categoryDepreciationCode").value(hasItem(DEFAULT_CATEGORY_DEPRECIATION_CODE)))
            .andExpect(jsonPath("$.[*].depreciationRegimeId").value(hasItem(DEFAULT_DEPRECIATION_REGIME_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getFixedAssetCategory() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get the fixedAssetCategory
        restFixedAssetCategoryMockMvc.perform(get("/api/fixed-asset-categories/{id}", fixedAssetCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fixedAssetCategory.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME))
            .andExpect(jsonPath("$.categoryDescription").value(DEFAULT_CATEGORY_DESCRIPTION))
            .andExpect(jsonPath("$.categoryAssetCode").value(DEFAULT_CATEGORY_ASSET_CODE))
            .andExpect(jsonPath("$.categoryDepreciationCode").value(DEFAULT_CATEGORY_DEPRECIATION_CODE))
            .andExpect(jsonPath("$.depreciationRegimeId").value(DEFAULT_DEPRECIATION_REGIME_ID.intValue()));
    }


    @Test
    @Transactional
    public void getFixedAssetCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        Long id = fixedAssetCategory.getId();

        defaultFixedAssetCategoryShouldBeFound("id.equals=" + id);
        defaultFixedAssetCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultFixedAssetCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFixedAssetCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultFixedAssetCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFixedAssetCategoryShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryName equals to DEFAULT_CATEGORY_NAME
        defaultFixedAssetCategoryShouldBeFound("categoryName.equals=" + DEFAULT_CATEGORY_NAME);

        // Get all the fixedAssetCategoryList where categoryName equals to UPDATED_CATEGORY_NAME
        defaultFixedAssetCategoryShouldNotBeFound("categoryName.equals=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryName not equals to DEFAULT_CATEGORY_NAME
        defaultFixedAssetCategoryShouldNotBeFound("categoryName.notEquals=" + DEFAULT_CATEGORY_NAME);

        // Get all the fixedAssetCategoryList where categoryName not equals to UPDATED_CATEGORY_NAME
        defaultFixedAssetCategoryShouldBeFound("categoryName.notEquals=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryName in DEFAULT_CATEGORY_NAME or UPDATED_CATEGORY_NAME
        defaultFixedAssetCategoryShouldBeFound("categoryName.in=" + DEFAULT_CATEGORY_NAME + "," + UPDATED_CATEGORY_NAME);

        // Get all the fixedAssetCategoryList where categoryName equals to UPDATED_CATEGORY_NAME
        defaultFixedAssetCategoryShouldNotBeFound("categoryName.in=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryName is not null
        defaultFixedAssetCategoryShouldBeFound("categoryName.specified=true");

        // Get all the fixedAssetCategoryList where categoryName is null
        defaultFixedAssetCategoryShouldNotBeFound("categoryName.specified=false");
    }
                @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryName contains DEFAULT_CATEGORY_NAME
        defaultFixedAssetCategoryShouldBeFound("categoryName.contains=" + DEFAULT_CATEGORY_NAME);

        // Get all the fixedAssetCategoryList where categoryName contains UPDATED_CATEGORY_NAME
        defaultFixedAssetCategoryShouldNotBeFound("categoryName.contains=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryName does not contain DEFAULT_CATEGORY_NAME
        defaultFixedAssetCategoryShouldNotBeFound("categoryName.doesNotContain=" + DEFAULT_CATEGORY_NAME);

        // Get all the fixedAssetCategoryList where categoryName does not contain UPDATED_CATEGORY_NAME
        defaultFixedAssetCategoryShouldBeFound("categoryName.doesNotContain=" + UPDATED_CATEGORY_NAME);
    }


    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryDescription equals to DEFAULT_CATEGORY_DESCRIPTION
        defaultFixedAssetCategoryShouldBeFound("categoryDescription.equals=" + DEFAULT_CATEGORY_DESCRIPTION);

        // Get all the fixedAssetCategoryList where categoryDescription equals to UPDATED_CATEGORY_DESCRIPTION
        defaultFixedAssetCategoryShouldNotBeFound("categoryDescription.equals=" + UPDATED_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryDescription not equals to DEFAULT_CATEGORY_DESCRIPTION
        defaultFixedAssetCategoryShouldNotBeFound("categoryDescription.notEquals=" + DEFAULT_CATEGORY_DESCRIPTION);

        // Get all the fixedAssetCategoryList where categoryDescription not equals to UPDATED_CATEGORY_DESCRIPTION
        defaultFixedAssetCategoryShouldBeFound("categoryDescription.notEquals=" + UPDATED_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryDescription in DEFAULT_CATEGORY_DESCRIPTION or UPDATED_CATEGORY_DESCRIPTION
        defaultFixedAssetCategoryShouldBeFound("categoryDescription.in=" + DEFAULT_CATEGORY_DESCRIPTION + "," + UPDATED_CATEGORY_DESCRIPTION);

        // Get all the fixedAssetCategoryList where categoryDescription equals to UPDATED_CATEGORY_DESCRIPTION
        defaultFixedAssetCategoryShouldNotBeFound("categoryDescription.in=" + UPDATED_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryDescription is not null
        defaultFixedAssetCategoryShouldBeFound("categoryDescription.specified=true");

        // Get all the fixedAssetCategoryList where categoryDescription is null
        defaultFixedAssetCategoryShouldNotBeFound("categoryDescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryDescriptionContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryDescription contains DEFAULT_CATEGORY_DESCRIPTION
        defaultFixedAssetCategoryShouldBeFound("categoryDescription.contains=" + DEFAULT_CATEGORY_DESCRIPTION);

        // Get all the fixedAssetCategoryList where categoryDescription contains UPDATED_CATEGORY_DESCRIPTION
        defaultFixedAssetCategoryShouldNotBeFound("categoryDescription.contains=" + UPDATED_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryDescription does not contain DEFAULT_CATEGORY_DESCRIPTION
        defaultFixedAssetCategoryShouldNotBeFound("categoryDescription.doesNotContain=" + DEFAULT_CATEGORY_DESCRIPTION);

        // Get all the fixedAssetCategoryList where categoryDescription does not contain UPDATED_CATEGORY_DESCRIPTION
        defaultFixedAssetCategoryShouldBeFound("categoryDescription.doesNotContain=" + UPDATED_CATEGORY_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryAssetCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryAssetCode equals to DEFAULT_CATEGORY_ASSET_CODE
        defaultFixedAssetCategoryShouldBeFound("categoryAssetCode.equals=" + DEFAULT_CATEGORY_ASSET_CODE);

        // Get all the fixedAssetCategoryList where categoryAssetCode equals to UPDATED_CATEGORY_ASSET_CODE
        defaultFixedAssetCategoryShouldNotBeFound("categoryAssetCode.equals=" + UPDATED_CATEGORY_ASSET_CODE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryAssetCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryAssetCode not equals to DEFAULT_CATEGORY_ASSET_CODE
        defaultFixedAssetCategoryShouldNotBeFound("categoryAssetCode.notEquals=" + DEFAULT_CATEGORY_ASSET_CODE);

        // Get all the fixedAssetCategoryList where categoryAssetCode not equals to UPDATED_CATEGORY_ASSET_CODE
        defaultFixedAssetCategoryShouldBeFound("categoryAssetCode.notEquals=" + UPDATED_CATEGORY_ASSET_CODE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryAssetCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryAssetCode in DEFAULT_CATEGORY_ASSET_CODE or UPDATED_CATEGORY_ASSET_CODE
        defaultFixedAssetCategoryShouldBeFound("categoryAssetCode.in=" + DEFAULT_CATEGORY_ASSET_CODE + "," + UPDATED_CATEGORY_ASSET_CODE);

        // Get all the fixedAssetCategoryList where categoryAssetCode equals to UPDATED_CATEGORY_ASSET_CODE
        defaultFixedAssetCategoryShouldNotBeFound("categoryAssetCode.in=" + UPDATED_CATEGORY_ASSET_CODE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryAssetCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryAssetCode is not null
        defaultFixedAssetCategoryShouldBeFound("categoryAssetCode.specified=true");

        // Get all the fixedAssetCategoryList where categoryAssetCode is null
        defaultFixedAssetCategoryShouldNotBeFound("categoryAssetCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryAssetCodeContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryAssetCode contains DEFAULT_CATEGORY_ASSET_CODE
        defaultFixedAssetCategoryShouldBeFound("categoryAssetCode.contains=" + DEFAULT_CATEGORY_ASSET_CODE);

        // Get all the fixedAssetCategoryList where categoryAssetCode contains UPDATED_CATEGORY_ASSET_CODE
        defaultFixedAssetCategoryShouldNotBeFound("categoryAssetCode.contains=" + UPDATED_CATEGORY_ASSET_CODE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryAssetCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryAssetCode does not contain DEFAULT_CATEGORY_ASSET_CODE
        defaultFixedAssetCategoryShouldNotBeFound("categoryAssetCode.doesNotContain=" + DEFAULT_CATEGORY_ASSET_CODE);

        // Get all the fixedAssetCategoryList where categoryAssetCode does not contain UPDATED_CATEGORY_ASSET_CODE
        defaultFixedAssetCategoryShouldBeFound("categoryAssetCode.doesNotContain=" + UPDATED_CATEGORY_ASSET_CODE);
    }


    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryDepreciationCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryDepreciationCode equals to DEFAULT_CATEGORY_DEPRECIATION_CODE
        defaultFixedAssetCategoryShouldBeFound("categoryDepreciationCode.equals=" + DEFAULT_CATEGORY_DEPRECIATION_CODE);

        // Get all the fixedAssetCategoryList where categoryDepreciationCode equals to UPDATED_CATEGORY_DEPRECIATION_CODE
        defaultFixedAssetCategoryShouldNotBeFound("categoryDepreciationCode.equals=" + UPDATED_CATEGORY_DEPRECIATION_CODE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryDepreciationCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryDepreciationCode not equals to DEFAULT_CATEGORY_DEPRECIATION_CODE
        defaultFixedAssetCategoryShouldNotBeFound("categoryDepreciationCode.notEquals=" + DEFAULT_CATEGORY_DEPRECIATION_CODE);

        // Get all the fixedAssetCategoryList where categoryDepreciationCode not equals to UPDATED_CATEGORY_DEPRECIATION_CODE
        defaultFixedAssetCategoryShouldBeFound("categoryDepreciationCode.notEquals=" + UPDATED_CATEGORY_DEPRECIATION_CODE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryDepreciationCodeIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryDepreciationCode in DEFAULT_CATEGORY_DEPRECIATION_CODE or UPDATED_CATEGORY_DEPRECIATION_CODE
        defaultFixedAssetCategoryShouldBeFound("categoryDepreciationCode.in=" + DEFAULT_CATEGORY_DEPRECIATION_CODE + "," + UPDATED_CATEGORY_DEPRECIATION_CODE);

        // Get all the fixedAssetCategoryList where categoryDepreciationCode equals to UPDATED_CATEGORY_DEPRECIATION_CODE
        defaultFixedAssetCategoryShouldNotBeFound("categoryDepreciationCode.in=" + UPDATED_CATEGORY_DEPRECIATION_CODE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryDepreciationCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryDepreciationCode is not null
        defaultFixedAssetCategoryShouldBeFound("categoryDepreciationCode.specified=true");

        // Get all the fixedAssetCategoryList where categoryDepreciationCode is null
        defaultFixedAssetCategoryShouldNotBeFound("categoryDepreciationCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryDepreciationCodeContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryDepreciationCode contains DEFAULT_CATEGORY_DEPRECIATION_CODE
        defaultFixedAssetCategoryShouldBeFound("categoryDepreciationCode.contains=" + DEFAULT_CATEGORY_DEPRECIATION_CODE);

        // Get all the fixedAssetCategoryList where categoryDepreciationCode contains UPDATED_CATEGORY_DEPRECIATION_CODE
        defaultFixedAssetCategoryShouldNotBeFound("categoryDepreciationCode.contains=" + UPDATED_CATEGORY_DEPRECIATION_CODE);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByCategoryDepreciationCodeNotContainsSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where categoryDepreciationCode does not contain DEFAULT_CATEGORY_DEPRECIATION_CODE
        defaultFixedAssetCategoryShouldNotBeFound("categoryDepreciationCode.doesNotContain=" + DEFAULT_CATEGORY_DEPRECIATION_CODE);

        // Get all the fixedAssetCategoryList where categoryDepreciationCode does not contain UPDATED_CATEGORY_DEPRECIATION_CODE
        defaultFixedAssetCategoryShouldBeFound("categoryDepreciationCode.doesNotContain=" + UPDATED_CATEGORY_DEPRECIATION_CODE);
    }


    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByDepreciationRegimeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where depreciationRegimeId equals to DEFAULT_DEPRECIATION_REGIME_ID
        defaultFixedAssetCategoryShouldBeFound("depreciationRegimeId.equals=" + DEFAULT_DEPRECIATION_REGIME_ID);

        // Get all the fixedAssetCategoryList where depreciationRegimeId equals to UPDATED_DEPRECIATION_REGIME_ID
        defaultFixedAssetCategoryShouldNotBeFound("depreciationRegimeId.equals=" + UPDATED_DEPRECIATION_REGIME_ID);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByDepreciationRegimeIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where depreciationRegimeId not equals to DEFAULT_DEPRECIATION_REGIME_ID
        defaultFixedAssetCategoryShouldNotBeFound("depreciationRegimeId.notEquals=" + DEFAULT_DEPRECIATION_REGIME_ID);

        // Get all the fixedAssetCategoryList where depreciationRegimeId not equals to UPDATED_DEPRECIATION_REGIME_ID
        defaultFixedAssetCategoryShouldBeFound("depreciationRegimeId.notEquals=" + UPDATED_DEPRECIATION_REGIME_ID);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByDepreciationRegimeIdIsInShouldWork() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where depreciationRegimeId in DEFAULT_DEPRECIATION_REGIME_ID or UPDATED_DEPRECIATION_REGIME_ID
        defaultFixedAssetCategoryShouldBeFound("depreciationRegimeId.in=" + DEFAULT_DEPRECIATION_REGIME_ID + "," + UPDATED_DEPRECIATION_REGIME_ID);

        // Get all the fixedAssetCategoryList where depreciationRegimeId equals to UPDATED_DEPRECIATION_REGIME_ID
        defaultFixedAssetCategoryShouldNotBeFound("depreciationRegimeId.in=" + UPDATED_DEPRECIATION_REGIME_ID);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByDepreciationRegimeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where depreciationRegimeId is not null
        defaultFixedAssetCategoryShouldBeFound("depreciationRegimeId.specified=true");

        // Get all the fixedAssetCategoryList where depreciationRegimeId is null
        defaultFixedAssetCategoryShouldNotBeFound("depreciationRegimeId.specified=false");
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByDepreciationRegimeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where depreciationRegimeId is greater than or equal to DEFAULT_DEPRECIATION_REGIME_ID
        defaultFixedAssetCategoryShouldBeFound("depreciationRegimeId.greaterThanOrEqual=" + DEFAULT_DEPRECIATION_REGIME_ID);

        // Get all the fixedAssetCategoryList where depreciationRegimeId is greater than or equal to UPDATED_DEPRECIATION_REGIME_ID
        defaultFixedAssetCategoryShouldNotBeFound("depreciationRegimeId.greaterThanOrEqual=" + UPDATED_DEPRECIATION_REGIME_ID);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByDepreciationRegimeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where depreciationRegimeId is less than or equal to DEFAULT_DEPRECIATION_REGIME_ID
        defaultFixedAssetCategoryShouldBeFound("depreciationRegimeId.lessThanOrEqual=" + DEFAULT_DEPRECIATION_REGIME_ID);

        // Get all the fixedAssetCategoryList where depreciationRegimeId is less than or equal to SMALLER_DEPRECIATION_REGIME_ID
        defaultFixedAssetCategoryShouldNotBeFound("depreciationRegimeId.lessThanOrEqual=" + SMALLER_DEPRECIATION_REGIME_ID);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByDepreciationRegimeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where depreciationRegimeId is less than DEFAULT_DEPRECIATION_REGIME_ID
        defaultFixedAssetCategoryShouldNotBeFound("depreciationRegimeId.lessThan=" + DEFAULT_DEPRECIATION_REGIME_ID);

        // Get all the fixedAssetCategoryList where depreciationRegimeId is less than UPDATED_DEPRECIATION_REGIME_ID
        defaultFixedAssetCategoryShouldBeFound("depreciationRegimeId.lessThan=" + UPDATED_DEPRECIATION_REGIME_ID);
    }

    @Test
    @Transactional
    public void getAllFixedAssetCategoriesByDepreciationRegimeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        // Get all the fixedAssetCategoryList where depreciationRegimeId is greater than DEFAULT_DEPRECIATION_REGIME_ID
        defaultFixedAssetCategoryShouldNotBeFound("depreciationRegimeId.greaterThan=" + DEFAULT_DEPRECIATION_REGIME_ID);

        // Get all the fixedAssetCategoryList where depreciationRegimeId is greater than SMALLER_DEPRECIATION_REGIME_ID
        defaultFixedAssetCategoryShouldBeFound("depreciationRegimeId.greaterThan=" + SMALLER_DEPRECIATION_REGIME_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFixedAssetCategoryShouldBeFound(String filter) throws Exception {
        restFixedAssetCategoryMockMvc.perform(get("/api/fixed-asset-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixedAssetCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].categoryDescription").value(hasItem(DEFAULT_CATEGORY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].categoryAssetCode").value(hasItem(DEFAULT_CATEGORY_ASSET_CODE)))
            .andExpect(jsonPath("$.[*].categoryDepreciationCode").value(hasItem(DEFAULT_CATEGORY_DEPRECIATION_CODE)))
            .andExpect(jsonPath("$.[*].depreciationRegimeId").value(hasItem(DEFAULT_DEPRECIATION_REGIME_ID.intValue())));

        // Check, that the count call also returns 1
        restFixedAssetCategoryMockMvc.perform(get("/api/fixed-asset-categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFixedAssetCategoryShouldNotBeFound(String filter) throws Exception {
        restFixedAssetCategoryMockMvc.perform(get("/api/fixed-asset-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFixedAssetCategoryMockMvc.perform(get("/api/fixed-asset-categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFixedAssetCategory() throws Exception {
        // Get the fixedAssetCategory
        restFixedAssetCategoryMockMvc.perform(get("/api/fixed-asset-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFixedAssetCategory() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        int databaseSizeBeforeUpdate = fixedAssetCategoryRepository.findAll().size();

        // Update the fixedAssetCategory
        FixedAssetCategory updatedFixedAssetCategory = fixedAssetCategoryRepository.findById(fixedAssetCategory.getId()).get();
        // Disconnect from session so that the updates on updatedFixedAssetCategory are not directly saved in db
        em.detach(updatedFixedAssetCategory);
        updatedFixedAssetCategory
            .categoryName(UPDATED_CATEGORY_NAME)
            .categoryDescription(UPDATED_CATEGORY_DESCRIPTION)
            .categoryAssetCode(UPDATED_CATEGORY_ASSET_CODE)
            .categoryDepreciationCode(UPDATED_CATEGORY_DEPRECIATION_CODE)
            .depreciationRegimeId(UPDATED_DEPRECIATION_REGIME_ID);
        FixedAssetCategoryDTO fixedAssetCategoryDTO = fixedAssetCategoryMapper.toDto(updatedFixedAssetCategory);

        restFixedAssetCategoryMockMvc.perform(put("/api/fixed-asset-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fixedAssetCategoryDTO)))
            .andExpect(status().isOk());

        // Validate the FixedAssetCategory in the database
        List<FixedAssetCategory> fixedAssetCategoryList = fixedAssetCategoryRepository.findAll();
        assertThat(fixedAssetCategoryList).hasSize(databaseSizeBeforeUpdate);
        FixedAssetCategory testFixedAssetCategory = fixedAssetCategoryList.get(fixedAssetCategoryList.size() - 1);
        assertThat(testFixedAssetCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testFixedAssetCategory.getCategoryDescription()).isEqualTo(UPDATED_CATEGORY_DESCRIPTION);
        assertThat(testFixedAssetCategory.getCategoryAssetCode()).isEqualTo(UPDATED_CATEGORY_ASSET_CODE);
        assertThat(testFixedAssetCategory.getCategoryDepreciationCode()).isEqualTo(UPDATED_CATEGORY_DEPRECIATION_CODE);
        assertThat(testFixedAssetCategory.getDepreciationRegimeId()).isEqualTo(UPDATED_DEPRECIATION_REGIME_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingFixedAssetCategory() throws Exception {
        int databaseSizeBeforeUpdate = fixedAssetCategoryRepository.findAll().size();

        // Create the FixedAssetCategory
        FixedAssetCategoryDTO fixedAssetCategoryDTO = fixedAssetCategoryMapper.toDto(fixedAssetCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFixedAssetCategoryMockMvc.perform(put("/api/fixed-asset-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fixedAssetCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FixedAssetCategory in the database
        List<FixedAssetCategory> fixedAssetCategoryList = fixedAssetCategoryRepository.findAll();
        assertThat(fixedAssetCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFixedAssetCategory() throws Exception {
        // Initialize the database
        fixedAssetCategoryRepository.saveAndFlush(fixedAssetCategory);

        int databaseSizeBeforeDelete = fixedAssetCategoryRepository.findAll().size();

        // Delete the fixedAssetCategory
        restFixedAssetCategoryMockMvc.perform(delete("/api/fixed-asset-categories/{id}", fixedAssetCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FixedAssetCategory> fixedAssetCategoryList = fixedAssetCategoryRepository.findAll();
        assertThat(fixedAssetCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
