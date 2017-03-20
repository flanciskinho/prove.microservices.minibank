package org.example.minibank.account.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.example.minibank.account.service.BankAccountService;
import org.example.minibank.account.service.dto.AmountDTO;
import org.example.minibank.account.web.rest.util.HeaderUtil;
import org.example.minibank.account.web.rest.util.PaginationUtil;
import org.example.minibank.account.service.dto.BankAccountDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing BankAccount.
 */
@RestController
@RequestMapping("/api")
public class BankAccountResource {

    private final Logger log = LoggerFactory.getLogger(BankAccountResource.class);

    @Inject
    private BankAccountService bankAccountService;


    /**
     * POST  /bank-accounts/add : add amount to a specific account
     */
    @RequestMapping(value = "/bank-accounts/add",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BankAccountDTO> addAmount(@Valid @RequestBody AmountDTO amountDTO) throws URISyntaxException {
        log.debug("REST request to save amountDTO : {}", amountDTO);

        BankAccountDTO result = bankAccountService.addAmount(amountDTO);
        return (result == null) ?
            ResponseEntity.badRequest().body(null):
            ResponseEntity.created(new URI("api//bank-accounts/" + result.getId())).body(result);
    }

    /**
     * POST  /bank-accounts/withdraw : withdraw amount to a specific account
     */
    @RequestMapping(value = "/bank-accounts/withdraw",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BankAccountDTO> witDraw(@Valid @RequestBody AmountDTO amountDTO) throws URISyntaxException {
        log.debug("REST request to save amountDTO : {}", amountDTO);

        BankAccountDTO result = bankAccountService.withdrawAmount(amountDTO);
        return (result == null) ?
            ResponseEntity.badRequest().body(null):
            ResponseEntity.created(new URI("api//bank-accounts/" + result.getId())).body(result);
    }

    /**
     * POST  /bank-accounts : Create a new bankAccount.
     *
     * @param bankAccountDTO the bankAccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bankAccountDTO, or with status 400 (Bad Request) if the bankAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bank-accounts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BankAccountDTO> createBankAccount(@Valid @RequestBody BankAccountDTO bankAccountDTO) throws URISyntaxException {
        log.debug("REST request to save BankAccount : {}", bankAccountDTO);
        if (bankAccountDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bankAccount", "idexists", "A new bankAccount cannot already have an ID")).body(null);
        }
        BankAccountDTO result = bankAccountService.save(bankAccountDTO);
        return (result == null) ?
            ResponseEntity.badRequest().body(null):
            ResponseEntity.created(new URI("/api/bank-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bankAccount", result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bank-accounts : get all the bankAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bankAccounts in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/bank-accounts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BankAccountDTO>> getAllBankAccounts(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BankAccounts");
        Page<BankAccountDTO> page = bankAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bank-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bank-accounts/:id : get the "id" bankAccount.
     *
     * @param id the id of the bankAccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bankAccountDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/bank-accounts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BankAccountDTO> getBankAccount(@PathVariable Long id) {
        log.debug("REST request to get BankAccount : {}", id);
        BankAccountDTO bankAccountDTO = bankAccountService.findOne(id);
        return Optional.ofNullable(bankAccountDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bank-accounts/:id : delete the "id" bankAccount.
     *
     * @param id the id of the bankAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/bank-accounts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBankAccount(@PathVariable Long id) {
        log.debug("REST request to delete BankAccount : {}", id);
        bankAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bankAccount", id.toString())).build();
    }

}
