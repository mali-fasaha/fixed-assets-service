package io.github.assets.web.rest;

import io.github.assets.FixedAssetServiceApp;
import io.github.assets.config.SecurityBeanOverrideConfiguration;
import io.github.assets.domain.MessageToken;
import io.github.assets.repository.MessageTokenRepository;
import io.github.assets.repository.search.MessageTokenSearchRepository;
import io.github.assets.service.MessageTokenService;
import io.github.assets.service.dto.MessageTokenDTO;
import io.github.assets.service.mapper.MessageTokenMapper;
import io.github.assets.web.rest.errors.ExceptionTranslator;
import io.github.assets.service.dto.MessageTokenCriteria;
import io.github.assets.service.MessageTokenQueryService;

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

/**
 * Integration tests for the {@Link MessageTokenResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, FixedAssetServiceApp.class})
public class MessageTokenResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_TIME_SENT = 1L;
    private static final Long UPDATED_TIME_SENT = 2L;

    private static final String DEFAULT_TOKEN_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN_VALUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RECEIVED = false;
    private static final Boolean UPDATED_RECEIVED = true;

    private static final Boolean DEFAULT_ACTIONED = false;
    private static final Boolean UPDATED_ACTIONED = true;

    private static final Boolean DEFAULT_CONTENT_FULLY_ENQUEUED = false;
    private static final Boolean UPDATED_CONTENT_FULLY_ENQUEUED = true;

    @Autowired
    private MessageTokenRepository messageTokenRepository;

    @Autowired
    private MessageTokenMapper messageTokenMapper;

    @Autowired
    private MessageTokenService messageTokenService;

    /**
     * This repository is mocked in the io.github.assets.repository.search test package.
     *
     * @see io.github.assets.repository.search.MessageTokenSearchRepositoryMockConfiguration
     */
    @Autowired
    private MessageTokenSearchRepository mockMessageTokenSearchRepository;

    @Autowired
    private MessageTokenQueryService messageTokenQueryService;

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

    private MockMvc restMessageTokenMockMvc;

    private MessageToken messageToken;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MessageTokenResource messageTokenResource = new MessageTokenResource(messageTokenService, messageTokenQueryService);
        this.restMessageTokenMockMvc = MockMvcBuilders.standaloneSetup(messageTokenResource)
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
    public static MessageToken createEntity(EntityManager em) {
        MessageToken messageToken = new MessageToken()
            .description(DEFAULT_DESCRIPTION)
            .timeSent(DEFAULT_TIME_SENT)
            .tokenValue(DEFAULT_TOKEN_VALUE)
            .received(DEFAULT_RECEIVED)
            .actioned(DEFAULT_ACTIONED)
            .contentFullyEnqueued(DEFAULT_CONTENT_FULLY_ENQUEUED);
        return messageToken;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MessageToken createUpdatedEntity(EntityManager em) {
        MessageToken messageToken = new MessageToken()
            .description(UPDATED_DESCRIPTION)
            .timeSent(UPDATED_TIME_SENT)
            .tokenValue(UPDATED_TOKEN_VALUE)
            .received(UPDATED_RECEIVED)
            .actioned(UPDATED_ACTIONED)
            .contentFullyEnqueued(UPDATED_CONTENT_FULLY_ENQUEUED);
        return messageToken;
    }

    @BeforeEach
    public void initTest() {
        messageToken = createEntity(em);
    }

    @Test
    @Transactional
    public void createMessageToken() throws Exception {
        int databaseSizeBeforeCreate = messageTokenRepository.findAll().size();

        // Create the MessageToken
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);
        restMessageTokenMockMvc.perform(post("/api/message-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO)))
            .andExpect(status().isCreated());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeCreate + 1);
        MessageToken testMessageToken = messageTokenList.get(messageTokenList.size() - 1);
        assertThat(testMessageToken.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMessageToken.getTimeSent()).isEqualTo(DEFAULT_TIME_SENT);
        assertThat(testMessageToken.getTokenValue()).isEqualTo(DEFAULT_TOKEN_VALUE);
        assertThat(testMessageToken.isReceived()).isEqualTo(DEFAULT_RECEIVED);
        assertThat(testMessageToken.isActioned()).isEqualTo(DEFAULT_ACTIONED);
        assertThat(testMessageToken.isContentFullyEnqueued()).isEqualTo(DEFAULT_CONTENT_FULLY_ENQUEUED);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(1)).save(testMessageToken);
    }

    @Test
    @Transactional
    public void createMessageTokenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = messageTokenRepository.findAll().size();

        // Create the MessageToken with an existing ID
        messageToken.setId(1L);
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageTokenMockMvc.perform(post("/api/message-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeCreate);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(0)).save(messageToken);
    }


    @Test
    @Transactional
    public void checkTimeSentIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageTokenRepository.findAll().size();
        // set the field null
        messageToken.setTimeSent(null);

        // Create the MessageToken, which fails.
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);

        restMessageTokenMockMvc.perform(post("/api/message-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO)))
            .andExpect(status().isBadRequest());

        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTokenValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageTokenRepository.findAll().size();
        // set the field null
        messageToken.setTokenValue(null);

        // Create the MessageToken, which fails.
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);

        restMessageTokenMockMvc.perform(post("/api/message-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO)))
            .andExpect(status().isBadRequest());

        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMessageTokens() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList
        restMessageTokenMockMvc.perform(get("/api/message-tokens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].timeSent").value(hasItem(DEFAULT_TIME_SENT.intValue())))
            .andExpect(jsonPath("$.[*].tokenValue").value(hasItem(DEFAULT_TOKEN_VALUE.toString())))
            .andExpect(jsonPath("$.[*].received").value(hasItem(DEFAULT_RECEIVED.booleanValue())))
            .andExpect(jsonPath("$.[*].actioned").value(hasItem(DEFAULT_ACTIONED.booleanValue())))
            .andExpect(jsonPath("$.[*].contentFullyEnqueued").value(hasItem(DEFAULT_CONTENT_FULLY_ENQUEUED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getMessageToken() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get the messageToken
        restMessageTokenMockMvc.perform(get("/api/message-tokens/{id}", messageToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(messageToken.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.timeSent").value(DEFAULT_TIME_SENT.intValue()))
            .andExpect(jsonPath("$.tokenValue").value(DEFAULT_TOKEN_VALUE.toString()))
            .andExpect(jsonPath("$.received").value(DEFAULT_RECEIVED.booleanValue()))
            .andExpect(jsonPath("$.actioned").value(DEFAULT_ACTIONED.booleanValue()))
            .andExpect(jsonPath("$.contentFullyEnqueued").value(DEFAULT_CONTENT_FULLY_ENQUEUED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllMessageTokensByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where description equals to DEFAULT_DESCRIPTION
        defaultMessageTokenShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the messageTokenList where description equals to UPDATED_DESCRIPTION
        defaultMessageTokenShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMessageTokenShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the messageTokenList where description equals to UPDATED_DESCRIPTION
        defaultMessageTokenShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where description is not null
        defaultMessageTokenShouldBeFound("description.specified=true");

        // Get all the messageTokenList where description is null
        defaultMessageTokenShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTimeSentIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent equals to DEFAULT_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.equals=" + DEFAULT_TIME_SENT);

        // Get all the messageTokenList where timeSent equals to UPDATED_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.equals=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTimeSentIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent in DEFAULT_TIME_SENT or UPDATED_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.in=" + DEFAULT_TIME_SENT + "," + UPDATED_TIME_SENT);

        // Get all the messageTokenList where timeSent equals to UPDATED_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.in=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTimeSentIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent is not null
        defaultMessageTokenShouldBeFound("timeSent.specified=true");

        // Get all the messageTokenList where timeSent is null
        defaultMessageTokenShouldNotBeFound("timeSent.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTimeSentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent greater than or equals to DEFAULT_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.greaterOrEqualThan=" + DEFAULT_TIME_SENT);

        // Get all the messageTokenList where timeSent greater than or equals to UPDATED_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.greaterOrEqualThan=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTimeSentIsLessThanSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent less than or equals to DEFAULT_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.lessThan=" + DEFAULT_TIME_SENT);

        // Get all the messageTokenList where timeSent less than or equals to UPDATED_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.lessThan=" + UPDATED_TIME_SENT);
    }


    @Test
    @Transactional
    public void getAllMessageTokensByTokenValueIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where tokenValue equals to DEFAULT_TOKEN_VALUE
        defaultMessageTokenShouldBeFound("tokenValue.equals=" + DEFAULT_TOKEN_VALUE);

        // Get all the messageTokenList where tokenValue equals to UPDATED_TOKEN_VALUE
        defaultMessageTokenShouldNotBeFound("tokenValue.equals=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTokenValueIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where tokenValue in DEFAULT_TOKEN_VALUE or UPDATED_TOKEN_VALUE
        defaultMessageTokenShouldBeFound("tokenValue.in=" + DEFAULT_TOKEN_VALUE + "," + UPDATED_TOKEN_VALUE);

        // Get all the messageTokenList where tokenValue equals to UPDATED_TOKEN_VALUE
        defaultMessageTokenShouldNotBeFound("tokenValue.in=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTokenValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where tokenValue is not null
        defaultMessageTokenShouldBeFound("tokenValue.specified=true");

        // Get all the messageTokenList where tokenValue is null
        defaultMessageTokenShouldNotBeFound("tokenValue.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessageTokensByReceivedIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where received equals to DEFAULT_RECEIVED
        defaultMessageTokenShouldBeFound("received.equals=" + DEFAULT_RECEIVED);

        // Get all the messageTokenList where received equals to UPDATED_RECEIVED
        defaultMessageTokenShouldNotBeFound("received.equals=" + UPDATED_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByReceivedIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where received in DEFAULT_RECEIVED or UPDATED_RECEIVED
        defaultMessageTokenShouldBeFound("received.in=" + DEFAULT_RECEIVED + "," + UPDATED_RECEIVED);

        // Get all the messageTokenList where received equals to UPDATED_RECEIVED
        defaultMessageTokenShouldNotBeFound("received.in=" + UPDATED_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByReceivedIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where received is not null
        defaultMessageTokenShouldBeFound("received.specified=true");

        // Get all the messageTokenList where received is null
        defaultMessageTokenShouldNotBeFound("received.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessageTokensByActionedIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where actioned equals to DEFAULT_ACTIONED
        defaultMessageTokenShouldBeFound("actioned.equals=" + DEFAULT_ACTIONED);

        // Get all the messageTokenList where actioned equals to UPDATED_ACTIONED
        defaultMessageTokenShouldNotBeFound("actioned.equals=" + UPDATED_ACTIONED);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByActionedIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where actioned in DEFAULT_ACTIONED or UPDATED_ACTIONED
        defaultMessageTokenShouldBeFound("actioned.in=" + DEFAULT_ACTIONED + "," + UPDATED_ACTIONED);

        // Get all the messageTokenList where actioned equals to UPDATED_ACTIONED
        defaultMessageTokenShouldNotBeFound("actioned.in=" + UPDATED_ACTIONED);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByActionedIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where actioned is not null
        defaultMessageTokenShouldBeFound("actioned.specified=true");

        // Get all the messageTokenList where actioned is null
        defaultMessageTokenShouldNotBeFound("actioned.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessageTokensByContentFullyEnqueuedIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where contentFullyEnqueued equals to DEFAULT_CONTENT_FULLY_ENQUEUED
        defaultMessageTokenShouldBeFound("contentFullyEnqueued.equals=" + DEFAULT_CONTENT_FULLY_ENQUEUED);

        // Get all the messageTokenList where contentFullyEnqueued equals to UPDATED_CONTENT_FULLY_ENQUEUED
        defaultMessageTokenShouldNotBeFound("contentFullyEnqueued.equals=" + UPDATED_CONTENT_FULLY_ENQUEUED);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByContentFullyEnqueuedIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where contentFullyEnqueued in DEFAULT_CONTENT_FULLY_ENQUEUED or UPDATED_CONTENT_FULLY_ENQUEUED
        defaultMessageTokenShouldBeFound("contentFullyEnqueued.in=" + DEFAULT_CONTENT_FULLY_ENQUEUED + "," + UPDATED_CONTENT_FULLY_ENQUEUED);

        // Get all the messageTokenList where contentFullyEnqueued equals to UPDATED_CONTENT_FULLY_ENQUEUED
        defaultMessageTokenShouldNotBeFound("contentFullyEnqueued.in=" + UPDATED_CONTENT_FULLY_ENQUEUED);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByContentFullyEnqueuedIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where contentFullyEnqueued is not null
        defaultMessageTokenShouldBeFound("contentFullyEnqueued.specified=true");

        // Get all the messageTokenList where contentFullyEnqueued is null
        defaultMessageTokenShouldNotBeFound("contentFullyEnqueued.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMessageTokenShouldBeFound(String filter) throws Exception {
        restMessageTokenMockMvc.perform(get("/api/message-tokens?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].timeSent").value(hasItem(DEFAULT_TIME_SENT.intValue())))
            .andExpect(jsonPath("$.[*].tokenValue").value(hasItem(DEFAULT_TOKEN_VALUE)))
            .andExpect(jsonPath("$.[*].received").value(hasItem(DEFAULT_RECEIVED.booleanValue())))
            .andExpect(jsonPath("$.[*].actioned").value(hasItem(DEFAULT_ACTIONED.booleanValue())))
            .andExpect(jsonPath("$.[*].contentFullyEnqueued").value(hasItem(DEFAULT_CONTENT_FULLY_ENQUEUED.booleanValue())));

        // Check, that the count call also returns 1
        restMessageTokenMockMvc.perform(get("/api/message-tokens/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMessageTokenShouldNotBeFound(String filter) throws Exception {
        restMessageTokenMockMvc.perform(get("/api/message-tokens?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMessageTokenMockMvc.perform(get("/api/message-tokens/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMessageToken() throws Exception {
        // Get the messageToken
        restMessageTokenMockMvc.perform(get("/api/message-tokens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessageToken() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        int databaseSizeBeforeUpdate = messageTokenRepository.findAll().size();

        // Update the messageToken
        MessageToken updatedMessageToken = messageTokenRepository.findById(messageToken.getId()).get();
        // Disconnect from session so that the updates on updatedMessageToken are not directly saved in db
        em.detach(updatedMessageToken);
        updatedMessageToken
            .description(UPDATED_DESCRIPTION)
            .timeSent(UPDATED_TIME_SENT)
            .tokenValue(UPDATED_TOKEN_VALUE)
            .received(UPDATED_RECEIVED)
            .actioned(UPDATED_ACTIONED)
            .contentFullyEnqueued(UPDATED_CONTENT_FULLY_ENQUEUED);
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(updatedMessageToken);

        restMessageTokenMockMvc.perform(put("/api/message-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO)))
            .andExpect(status().isOk());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeUpdate);
        MessageToken testMessageToken = messageTokenList.get(messageTokenList.size() - 1);
        assertThat(testMessageToken.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMessageToken.getTimeSent()).isEqualTo(UPDATED_TIME_SENT);
        assertThat(testMessageToken.getTokenValue()).isEqualTo(UPDATED_TOKEN_VALUE);
        assertThat(testMessageToken.isReceived()).isEqualTo(UPDATED_RECEIVED);
        assertThat(testMessageToken.isActioned()).isEqualTo(UPDATED_ACTIONED);
        assertThat(testMessageToken.isContentFullyEnqueued()).isEqualTo(UPDATED_CONTENT_FULLY_ENQUEUED);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(1)).save(testMessageToken);
    }

    @Test
    @Transactional
    public void updateNonExistingMessageToken() throws Exception {
        int databaseSizeBeforeUpdate = messageTokenRepository.findAll().size();

        // Create the MessageToken
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageTokenMockMvc.perform(put("/api/message-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(0)).save(messageToken);
    }

    @Test
    @Transactional
    public void deleteMessageToken() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        int databaseSizeBeforeDelete = messageTokenRepository.findAll().size();

        // Delete the messageToken
        restMessageTokenMockMvc.perform(delete("/api/message-tokens/{id}", messageToken.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(1)).deleteById(messageToken.getId());
    }

    @Test
    @Transactional
    public void searchMessageToken() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);
        when(mockMessageTokenSearchRepository.search(queryStringQuery("id:" + messageToken.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(messageToken), PageRequest.of(0, 1), 1));
        // Search the messageToken
        restMessageTokenMockMvc.perform(get("/api/_search/message-tokens?query=id:" + messageToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].timeSent").value(hasItem(DEFAULT_TIME_SENT.intValue())))
            .andExpect(jsonPath("$.[*].tokenValue").value(hasItem(DEFAULT_TOKEN_VALUE)))
            .andExpect(jsonPath("$.[*].received").value(hasItem(DEFAULT_RECEIVED.booleanValue())))
            .andExpect(jsonPath("$.[*].actioned").value(hasItem(DEFAULT_ACTIONED.booleanValue())))
            .andExpect(jsonPath("$.[*].contentFullyEnqueued").value(hasItem(DEFAULT_CONTENT_FULLY_ENQUEUED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageToken.class);
        MessageToken messageToken1 = new MessageToken();
        messageToken1.setId(1L);
        MessageToken messageToken2 = new MessageToken();
        messageToken2.setId(messageToken1.getId());
        assertThat(messageToken1).isEqualTo(messageToken2);
        messageToken2.setId(2L);
        assertThat(messageToken1).isNotEqualTo(messageToken2);
        messageToken1.setId(null);
        assertThat(messageToken1).isNotEqualTo(messageToken2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MessageTokenDTO.class);
        MessageTokenDTO messageTokenDTO1 = new MessageTokenDTO();
        messageTokenDTO1.setId(1L);
        MessageTokenDTO messageTokenDTO2 = new MessageTokenDTO();
        assertThat(messageTokenDTO1).isNotEqualTo(messageTokenDTO2);
        messageTokenDTO2.setId(messageTokenDTO1.getId());
        assertThat(messageTokenDTO1).isEqualTo(messageTokenDTO2);
        messageTokenDTO2.setId(2L);
        assertThat(messageTokenDTO1).isNotEqualTo(messageTokenDTO2);
        messageTokenDTO1.setId(null);
        assertThat(messageTokenDTO1).isNotEqualTo(messageTokenDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(messageTokenMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(messageTokenMapper.fromId(null)).isNull();
    }
}
