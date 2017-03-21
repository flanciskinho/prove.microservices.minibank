package org.example.minibank.accountoperation.service;

import org.example.minibank.accountoperation.domain.AccountOperation;
import org.example.minibank.accountoperation.repository.AccountOperationRepository;
import org.example.minibank.accountoperation.service.dto.AccountOperationDTO;
import org.example.minibank.accountoperation.service.mapper.AccountOperationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing AccountOperation.
 */
@Service
@Transactional
public class AccountOperationService {

    private final Logger log = LoggerFactory.getLogger(AccountOperationService.class);
    
    @Inject
    private AccountOperationRepository accountOperationRepository;

    @Inject
    private AccountOperationMapper accountOperationMapper;

    /**
     * Save a accountOperation.
     *
     * @param accountOperationDTO the entity to save
     * @return the persisted entity
     */
    public AccountOperationDTO save(AccountOperationDTO accountOperationDTO) {
        log.debug("Request to save AccountOperation : {}", accountOperationDTO);
        accountOperationDTO.setDate(ZonedDateTime.now());
        AccountOperation accountOperation = accountOperationMapper.accountOperationDTOToAccountOperation(accountOperationDTO);
        accountOperation = accountOperationRepository.save(accountOperation);
        AccountOperationDTO result = accountOperationMapper.accountOperationToAccountOperationDTO(accountOperation);
        return result;
    }

    /**
     *  Get all the accountOperations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<AccountOperationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccountOperations");
        Page<AccountOperation> result = accountOperationRepository.findAll(pageable);
        return result.map(accountOperation -> accountOperationMapper.accountOperationToAccountOperationDTO(accountOperation));
    }

    /**
     *  Get one accountOperation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AccountOperationDTO findOne(Long id) {
        log.debug("Request to get AccountOperation : {}", id);
        AccountOperation accountOperation = accountOperationRepository.findOne(id);
        AccountOperationDTO accountOperationDTO = accountOperationMapper.accountOperationToAccountOperationDTO(accountOperation);
        return accountOperationDTO;
    }

    /**
     *  Delete the  accountOperation by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AccountOperation : {}", id);
        accountOperationRepository.delete(id);
    }
}
