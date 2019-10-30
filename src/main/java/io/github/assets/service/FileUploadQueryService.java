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

import io.github.assets.domain.FileUpload;
import io.github.assets.domain.*; // for static metamodels
import io.github.assets.repository.FileUploadRepository;
import io.github.assets.repository.search.FileUploadSearchRepository;
import io.github.assets.service.dto.FileUploadCriteria;
import io.github.assets.service.dto.FileUploadDTO;
import io.github.assets.service.mapper.FileUploadMapper;

/**
 * Service for executing complex queries for {@link FileUpload} entities in the database.
 * The main input is a {@link FileUploadCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FileUploadDTO} or a {@link Page} of {@link FileUploadDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FileUploadQueryService extends QueryService<FileUpload> {

    private final Logger log = LoggerFactory.getLogger(FileUploadQueryService.class);

    private final FileUploadRepository fileUploadRepository;

    private final FileUploadMapper fileUploadMapper;

    private final FileUploadSearchRepository fileUploadSearchRepository;

    public FileUploadQueryService(FileUploadRepository fileUploadRepository, FileUploadMapper fileUploadMapper, FileUploadSearchRepository fileUploadSearchRepository) {
        this.fileUploadRepository = fileUploadRepository;
        this.fileUploadMapper = fileUploadMapper;
        this.fileUploadSearchRepository = fileUploadSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FileUploadDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FileUploadDTO> findByCriteria(FileUploadCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FileUpload> specification = createSpecification(criteria);
        return fileUploadMapper.toDto(fileUploadRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FileUploadDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FileUploadDTO> findByCriteria(FileUploadCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FileUpload> specification = createSpecification(criteria);
        return fileUploadRepository.findAll(specification, page)
            .map(fileUploadMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FileUploadCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FileUpload> specification = createSpecification(criteria);
        return fileUploadRepository.count(specification);
    }

    /**
     * Function to convert FileUploadCriteria to a {@link Specification}.
     */
    private Specification<FileUpload> createSpecification(FileUploadCriteria criteria) {
        Specification<FileUpload> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), FileUpload_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), FileUpload_.description));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), FileUpload_.fileName));
            }
            if (criteria.getPeriodFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeriodFrom(), FileUpload_.periodFrom));
            }
            if (criteria.getPeriodTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeriodTo(), FileUpload_.periodTo));
            }
            if (criteria.getFileTypeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFileTypeId(), FileUpload_.fileTypeId));
            }
            if (criteria.getUploadSuccessful() != null) {
                specification = specification.and(buildSpecification(criteria.getUploadSuccessful(), FileUpload_.uploadSuccessful));
            }
            if (criteria.getUploadProcessed() != null) {
                specification = specification.and(buildSpecification(criteria.getUploadProcessed(), FileUpload_.uploadProcessed));
            }
            if (criteria.getUploadToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUploadToken(), FileUpload_.uploadToken));
            }
        }
        return specification;
    }
}
