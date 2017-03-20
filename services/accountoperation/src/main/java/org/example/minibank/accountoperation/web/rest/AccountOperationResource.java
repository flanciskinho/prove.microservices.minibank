package org.example.minibank.accountoperation.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.example.minibank.accountoperation.service.AccountOperationService;
import org.example.minibank.accountoperation.web.rest.util.HeaderUtil;
import org.example.minibank.accountoperation.web.rest.util.PaginationUtil;
import org.example.minibank.accountoperation.service.dto.AccountOperationDTO;
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
 * REST controller for managing AccountOperation.
 */
@RestController
@RequestMapping("/api")
public class AccountOperationResource {

    private final Logger log = LoggerFactory.getLogger(AccountOperationResource.class);

    @Inject
    private AccountOperationService accountOperationService;

    /**
     * POST  /account-operations : Create a new accountOperation.
     *
     * @param accountOperationDTO the accountOperationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountOperationDTO, or with status 400 (Bad Request) if the accountOperation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/account-operations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountOperationDTO> createAccountOperation(@Valid @RequestBody AccountOperationDTO accountOperationDTO) throws URISyntaxException {
        log.debug("REST request to save AccountOperation : {}", accountOperationDTO);
        if (accountOperationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("accountOperation", "idexists", "A new accountOperation cannot already have an ID")).body(null);
        }
        AccountOperationDTO result = accountOperationService.save(accountOperationDTO);
        return ResponseEntity.created(new URI("/api/account-operations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("accountOperation", result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /account-operations : get all the accountOperations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of accountOperations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/account-operations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AccountOperationDTO>> getAllAccountOperations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AccountOperations");
        Page<AccountOperationDTO> page = accountOperationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/account-operations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /account-operations/:id : get the "id" accountOperation.
     *
     * @param id the id of the accountOperationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountOperationDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/account-operations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountOperationDTO> getAccountOperation(@PathVariable Long id) {
        log.debug("REST request to get AccountOperation : {}", id);
        AccountOperationDTO accountOperationDTO = accountOperationService.findOne(id);
        return Optional.ofNullable(accountOperationDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
