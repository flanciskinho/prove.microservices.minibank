package org.example.minibank.account.service;

import org.example.minibank.account.domain.BankAccount;
import org.example.minibank.account.repository.BankAccountRepository;
import org.example.minibank.account.security.SecurityUtils;
import org.example.minibank.account.service.dto.AmountDTO;
import org.example.minibank.account.service.dto.BankAccountDTO;
import org.example.minibank.account.service.dto.OperationDTO;
import org.example.minibank.account.service.mapper.BankAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
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
    private AccountOperationService accountOperationService;

    @Inject
    private BankAccountRepository bankAccountRepository;

    @Inject
    private BankAccountMapper bankAccountMapper;

    private OperationDTO createOperation(AmountDTO amountDTO, OperationDTO.AccountOperationType type) {
        OperationDTO operationDTO = new OperationDTO(null, amountDTO.getAccountId(), amountDTO.getAmount(), type);
        ResponseEntity<OperationDTO> responseEntity = accountOperationService.createOperation(operationDTO, SecurityUtils.getJwtToken());

        if (responseEntity == null || !responseEntity.getStatusCode().equals(HttpStatus.CREATED))
            return null;

        return responseEntity.getBody();
    }

    public BankAccountDTO addAmount(AmountDTO amountDTO) {
        log.debug("Request to addAmount. AmountDTO {}", amountDTO);

        if (amountDTO.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.debug("INVALID. Try to add negative amount {}", amountDTO.getAmount());
            return null;
        }

        BankAccount bankAccount = bankAccountRepository.findOne(amountDTO.getAccountId());
        if (bankAccount == null) {
            log.debug("INVALID. Try to add on non-exit account {}", amountDTO.getAccountId());
            return null;
        }

        if (createOperation(amountDTO, OperationDTO.AccountOperationType.ADD) == null)
            return null;

        bankAccount.setBalance(bankAccount.getBalance().add(amountDTO.getAmount()));

        bankAccount = bankAccountRepository.save(bankAccount);

        return bankAccountMapper.bankAccountToBankAccountDTO(bankAccount);
    }

    public BankAccountDTO withdrawAmount(AmountDTO amountDTO) {
        log.debug("Request to withdraw. AmountDTO {}", amountDTO);

        if (amountDTO.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.debug("INVALID. Try to add negative amount {}", amountDTO.getAmount());
            return null;
        }

        BankAccount bankAccount = bankAccountRepository.findOne(amountDTO.getAccountId());
        if (bankAccount == null) {
            log.debug("INVALID. Try to add on non-exit account {}", amountDTO.getAccountId());
            return null;
        }

        BigDecimal tmp = bankAccount.getBalance();
        if (tmp.compareTo(amountDTO.getAmount()) == -1) {
            log.debug("INVALID. Don't have enough {}-{}={}", tmp, amountDTO.getAmount(), tmp.subtract(amountDTO.getAmount()));
            return null;
        }

        if (createOperation(amountDTO, OperationDTO.AccountOperationType.WITHDRAW) == null)
            return null;

        bankAccount.setBalance(tmp.subtract(amountDTO.getAmount()));

        bankAccount = bankAccountRepository.save(bankAccount);

        return bankAccountMapper.bankAccountToBankAccountDTO(bankAccount);
    }

    /**
     * Save a bankAccount.
     *
     * @param bankAccountDTO the entity to save
     * @return the persisted entity
     */
    public BankAccountDTO save(BankAccountDTO bankAccountDTO) {
        log.debug("Request to save BankAccount : {}", bankAccountDTO);

        if (bankAccountDTO.getId() != null) {
            log.debug("INVALID. cannot create account with specific id");
            return null;
        }

        BankAccount bankAccount = bankAccountMapper.bankAccountDTOToBankAccount(bankAccountDTO);
        bankAccount.setUserId(SecurityUtils.getCurrentUserId());

        BigDecimal amount = bankAccountDTO.getBalance();
        if (amount.compareTo(BigDecimal.ZERO) == 1) {
            bankAccount.setBalance(BigDecimal.ZERO);
            bankAccount = bankAccountRepository.save(bankAccount);
            AmountDTO amountDTO = new AmountDTO(bankAccount.getId(), bankAccountDTO.getBalance());
            if (createOperation(amountDTO, OperationDTO.AccountOperationType.ADD) == null)
                return null;
            bankAccount.setBalance(amount);
        }

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
