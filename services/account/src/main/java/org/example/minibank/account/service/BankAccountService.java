package org.example.minibank.account.service;

import org.example.minibank.account.domain.BankAccount;
import org.example.minibank.account.repository.BankAccountRepository;
import org.example.minibank.account.service.dto.BankAccountDTO;
import org.example.minibank.account.service.mapper.BankAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing BankAccount.
 */
@Service
@Transactional
public class BankAccountService {

    private final Logger log = LoggerFactory.getLogger(BankAccountService.class);
    
    @Inject
    private BankAccountRepository bankAccountRepository;

    @Inject
    private BankAccountMapper bankAccountMapper;

    /**
     * Save a bankAccount.
     *
     * @param bankAccountDTO the entity to save
     * @return the persisted entity
     */
    public BankAccountDTO save(BankAccountDTO bankAccountDTO) {
        log.debug("Request to save BankAccount : {}", bankAccountDTO);
        BankAccount bankAccount = bankAccountMapper.bankAccountDTOToBankAccount(bankAccountDTO);
        bankAccount = bankAccountRepository.save(bankAccount);
        BankAccountDTO result = bankAccountMapper.bankAccountToBankAccountDTO(bankAccount);
        return result;
    }

    /**
     *  Get all the bankAccounts.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BankAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BankAccounts");
        Page<BankAccount> result = bankAccountRepository.findAll(pageable);
        return result.map(bankAccount -> bankAccountMapper.bankAccountToBankAccountDTO(bankAccount));
    }

    /**
     *  Get one bankAccount by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BankAccountDTO findOne(Long id) {
        log.debug("Request to get BankAccount : {}", id);
        BankAccount bankAccount = bankAccountRepository.findOne(id);
        BankAccountDTO bankAccountDTO = bankAccountMapper.bankAccountToBankAccountDTO(bankAccount);
        return bankAccountDTO;
    }

    /**
     *  Delete the  bankAccount by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BankAccount : {}", id);
        bankAccountRepository.delete(id);
    }
}
