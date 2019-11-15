package io.github.assets.service;

import io.github.assets.service.dto.MessageTokenDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.assets.domain.MessageToken}.
 */
public interface MessageTokenService {

    /**
     * Save a messageToken.
     *
     * @param messageTokenDTO the entity to save.
     * @return the persisted entity.
     */
    MessageTokenDTO save(MessageTokenDTO messageTokenDTO);

    /**
     * Get all the messageTokens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MessageTokenDTO> findAll(Pageable pageable);


    /**
     * Get the "id" messageToken.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MessageTokenDTO> findOne(Long id);

    /**
     * Delete the "id" messageToken.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
