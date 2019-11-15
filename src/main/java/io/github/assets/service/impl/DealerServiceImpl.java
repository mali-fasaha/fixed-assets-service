package io.github.assets.service.impl;

import io.github.assets.service.DealerService;
import io.github.assets.domain.Dealer;
import io.github.assets.repository.DealerRepository;
import io.github.assets.service.dto.DealerDTO;
import io.github.assets.service.mapper.DealerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Dealer}.
 */
@Service
@Transactional
public class DealerServiceImpl implements DealerService {

    private final Logger log = LoggerFactory.getLogger(DealerServiceImpl.class);

    private final DealerRepository dealerRepository;

    private final DealerMapper dealerMapper;

    public DealerServiceImpl(DealerRepository dealerRepository, DealerMapper dealerMapper) {
        this.dealerRepository = dealerRepository;
        this.dealerMapper = dealerMapper;
    }

    /**
     * Save a dealer.
     *
     * @param dealerDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DealerDTO save(DealerDTO dealerDTO) {
        log.debug("Request to save Dealer : {}", dealerDTO);
        Dealer dealer = dealerMapper.toEntity(dealerDTO);
        dealer = dealerRepository.save(dealer);
        return dealerMapper.toDto(dealer);
    }

    /**
     * Get all the dealers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DealerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dealers");
        return dealerRepository.findAll(pageable)
            .map(dealerMapper::toDto);
    }


    /**
     * Get one dealer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DealerDTO> findOne(Long id) {
        log.debug("Request to get Dealer : {}", id);
        return dealerRepository.findById(id)
            .map(dealerMapper::toDto);
    }

    /**
     * Delete the dealer by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dealer : {}", id);
        dealerRepository.deleteById(id);
    }
}
