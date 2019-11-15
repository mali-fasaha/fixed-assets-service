package io.github.assets.service.impl;

import io.github.assets.service.MessageTokenService;
import io.github.assets.domain.MessageToken;
import io.github.assets.repository.MessageTokenRepository;
import io.github.assets.service.dto.MessageTokenDTO;
import io.github.assets.service.mapper.MessageTokenMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link MessageToken}.
 */
@Service
@Transactional
public class MessageTokenServiceImpl implements MessageTokenService {

    private final Logger log = LoggerFactory.getLogger(MessageTokenServiceImpl.class);

    private final MessageTokenRepository messageTokenRepository;

    private final MessageTokenMapper messageTokenMapper;

    public MessageTokenServiceImpl(MessageTokenRepository messageTokenRepository, MessageTokenMapper messageTokenMapper) {
        this.messageTokenRepository = messageTokenRepository;
        this.messageTokenMapper = messageTokenMapper;
    }

    /**
     * Save a messageToken.
     *
     * @param messageTokenDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public MessageTokenDTO save(MessageTokenDTO messageTokenDTO) {
        log.debug("Request to save MessageToken : {}", messageTokenDTO);
        MessageToken messageToken = messageTokenMapper.toEntity(messageTokenDTO);
        messageToken = messageTokenRepository.save(messageToken);
        return messageTokenMapper.toDto(messageToken);
    }

    /**
     * Get all the messageTokens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MessageTokenDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MessageTokens");
        return messageTokenRepository.findAll(pageable)
            .map(messageTokenMapper::toDto);
    }


    /**
     * Get one messageToken by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MessageTokenDTO> findOne(Long id) {
        log.debug("Request to get MessageToken : {}", id);
        return messageTokenRepository.findById(id)
            .map(messageTokenMapper::toDto);
    }

    /**
     * Delete the messageToken by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MessageToken : {}", id);
        messageTokenRepository.deleteById(id);
    }
}
