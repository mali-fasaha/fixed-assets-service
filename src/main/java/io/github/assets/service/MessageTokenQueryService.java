package io.github.assets.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.github.assets.domain.MessageToken;
import io.github.assets.domain.*; // for static metamodels
import io.github.assets.repository.MessageTokenRepository;
import io.github.assets.service.dto.MessageTokenCriteria;
import io.github.assets.service.dto.MessageTokenDTO;
import io.github.assets.service.mapper.MessageTokenMapper;

/**
 * Service for executing complex queries for {@link MessageToken} entities in the database.
 * The main input is a {@link MessageTokenCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MessageTokenDTO} or a {@link Page} of {@link MessageTokenDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MessageTokenQueryService extends QueryService<MessageToken> {

    private final Logger log = LoggerFactory.getLogger(MessageTokenQueryService.class);

    private final MessageTokenRepository messageTokenRepository;

    private final MessageTokenMapper messageTokenMapper;

    public MessageTokenQueryService(MessageTokenRepository messageTokenRepository, MessageTokenMapper messageTokenMapper) {
        this.messageTokenRepository = messageTokenRepository;
        this.messageTokenMapper = messageTokenMapper;
    }

    /**
     * Return a {@link List} of {@link MessageTokenDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MessageTokenDTO> findByCriteria(MessageTokenCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MessageToken> specification = createSpecification(criteria);
        return messageTokenMapper.toDto(messageTokenRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MessageTokenDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MessageTokenDTO> findByCriteria(MessageTokenCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MessageToken> specification = createSpecification(criteria);
        return messageTokenRepository.findAll(specification, page)
            .map(messageTokenMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MessageTokenCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MessageToken> specification = createSpecification(criteria);
        return messageTokenRepository.count(specification);
    }

    /**
     * Function to convert {@link MessageTokenCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MessageToken> createSpecification(MessageTokenCriteria criteria) {
        Specification<MessageToken> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MessageToken_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), MessageToken_.description));
            }
            if (criteria.getTimeSent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeSent(), MessageToken_.timeSent));
            }
            if (criteria.getTokenValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTokenValue(), MessageToken_.tokenValue));
            }
            if (criteria.getReceived() != null) {
                specification = specification.and(buildSpecification(criteria.getReceived(), MessageToken_.received));
            }
            if (criteria.getActioned() != null) {
                specification = specification.and(buildSpecification(criteria.getActioned(), MessageToken_.actioned));
            }
            if (criteria.getContentFullyEnqueued() != null) {
                specification = specification.and(buildSpecification(criteria.getContentFullyEnqueued(), MessageToken_.contentFullyEnqueued));
            }
        }
        return specification;
    }
}
