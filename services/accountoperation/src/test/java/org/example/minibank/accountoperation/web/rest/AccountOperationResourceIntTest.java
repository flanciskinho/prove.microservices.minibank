package org.example.minibank.accountoperation.web.rest;

import org.example.minibank.accountoperation.AccountoperationApp;

import org.example.minibank.accountoperation.domain.AccountOperation;
import org.example.minibank.accountoperation.repository.AccountOperationRepository;
import org.example.minibank.accountoperation.service.AccountOperationService;
import org.example.minibank.accountoperation.service.dto.AccountOperationDTO;
import org.example.minibank.accountoperation.service.mapper.AccountOperationMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.example.minibank.accountoperation.domain.enumeration.AccountOperationType;
/**
 * Test class for the AccountOperationResource REST controller.
 *
 * @see AccountOperationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountoperationApp.class)
public class AccountOperationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_ACCOUNT_ID = 1L;
    private static final Long UPDATED_ACCOUNT_ID = 2L;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.now();
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);

    private static final AccountOperationType DEFAULT_TYPE = AccountOperationType.ADD;
    private static final AccountOperationType UPDATED_TYPE = AccountOperationType.WITHDRAW;

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    @Inject
    private AccountOperationRepository accountOperationRepository;

    @Inject
    private AccountOperationMapper accountOperationMapper;

    @Inject
    private AccountOperationService accountOperationService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAccountOperationMockMvc;

    private AccountOperation accountOperation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AccountOperationResource accountOperationResource = new AccountOperationResource();
        ReflectionTestUtils.setField(accountOperationResource, "accountOperationService", accountOperationService);
        this.restAccountOperationMockMvc = MockMvcBuilders.standaloneSetup(accountOperationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountOperation createEntity(EntityManager em) {
        AccountOperation accountOperation = new AccountOperation()
                .accountId(DEFAULT_ACCOUNT_ID)
                .date(DEFAULT_DATE)
                .type(DEFAULT_TYPE)
                .amount(DEFAULT_AMOUNT);
        return accountOperation;
    }

    @Before
    public void initTest() {
        accountOperation = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccountOperation() throws Exception {
        int databaseSizeBeforeCreate = accountOperationRepository.findAll().size();

        // Create the AccountOperation
        AccountOperationDTO accountOperationDTO = accountOperationMapper.accountOperationToAccountOperationDTO(accountOperation);

        restAccountOperationMockMvc.perform(post("/api/account-operations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountOperationDTO)))
                .andExpect(status().isCreated());

        // Validate the AccountOperation in the database
        List<AccountOperation> accountOperations = accountOperationRepository.findAll();
        assertThat(accountOperations).hasSize(databaseSizeBeforeCreate + 1);
        AccountOperation testAccountOperation = accountOperations.get(accountOperations.size() - 1);
        assertThat(testAccountOperation.getAccountId()).isEqualTo(DEFAULT_ACCOUNT_ID);
        assertThat(testAccountOperation.getDate()).isAfterOrEqualTo(DEFAULT_DATE);
        assertThat(testAccountOperation.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAccountOperation.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void checkAccountIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountOperationRepository.findAll().size();
        // set the field null
        accountOperation.setAccountId(null);

        // Create the AccountOperation, which fails.
        AccountOperationDTO accountOperationDTO = accountOperationMapper.accountOperationToAccountOperationDTO(accountOperation);

        restAccountOperationMockMvc.perform(post("/api/account-operations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountOperationDTO)))
                .andExpect(status().isBadRequest());

        List<AccountOperation> accountOperations = accountOperationRepository.findAll();
        assertThat(accountOperations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountOperationRepository.findAll().size();
        // set the field null
        accountOperation.setType(null);

        // Create the AccountOperation, which fails.
        AccountOperationDTO accountOperationDTO = accountOperationMapper.accountOperationToAccountOperationDTO(accountOperation);

        restAccountOperationMockMvc.perform(post("/api/account-operations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountOperationDTO)))
                .andExpect(status().isBadRequest());

        List<AccountOperation> accountOperations = accountOperationRepository.findAll();
        assertThat(accountOperations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountOperationRepository.findAll().size();
        // set the field null
        accountOperation.setAmount(null);

        // Create the AccountOperation, which fails.
        AccountOperationDTO accountOperationDTO = accountOperationMapper.accountOperationToAccountOperationDTO(accountOperation);

        restAccountOperationMockMvc.perform(post("/api/account-operations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountOperationDTO)))
                .andExpect(status().isBadRequest());

        List<AccountOperation> accountOperations = accountOperationRepository.findAll();
        assertThat(accountOperations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAccountOperations() throws Exception {
        // Initialize the database
        accountOperationRepository.saveAndFlush(accountOperation);

        // Get all the accountOperations
        restAccountOperationMockMvc.perform(get("/api/account-operations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(accountOperation.getId().intValue())))
                .andExpect(jsonPath("$.[*].accountId").value(hasItem(DEFAULT_ACCOUNT_ID.intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getAccountOperation() throws Exception {
        // Initialize the database
        accountOperationRepository.saveAndFlush(accountOperation);

        // Get the accountOperation
        restAccountOperationMockMvc.perform(get("/api/account-operations/{id}", accountOperation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accountOperation.getId().intValue()))
            .andExpect(jsonPath("$.accountId").value(DEFAULT_ACCOUNT_ID.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAccountOperation() throws Exception {
        // Get the accountOperation
        restAccountOperationMockMvc.perform(get("/api/account-operations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountOperation() throws Exception {
        // Initialize the database
        accountOperationRepository.saveAndFlush(accountOperation);
        int databaseSizeBeforeUpdate = accountOperationRepository.findAll().size();

        // Update the accountOperation
        AccountOperation updatedAccountOperation = accountOperationRepository.findOne(accountOperation.getId());
        updatedAccountOperation
                .accountId(UPDATED_ACCOUNT_ID)
                .date(UPDATED_DATE)
                .type(UPDATED_TYPE)
                .amount(UPDATED_AMOUNT);
        AccountOperationDTO accountOperationDTO = accountOperationMapper.accountOperationToAccountOperationDTO(updatedAccountOperation);

        restAccountOperationMockMvc.perform(put("/api/account-operations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accountOperationDTO)))
                .andExpect(status().isOk());

        // Validate the AccountOperation in the database
        List<AccountOperation> accountOperations = accountOperationRepository.findAll();
        assertThat(accountOperations).hasSize(databaseSizeBeforeUpdate);
        AccountOperation testAccountOperation = accountOperations.get(accountOperations.size() - 1);
        assertThat(testAccountOperation.getAccountId()).isEqualTo(UPDATED_ACCOUNT_ID);
        assertThat(testAccountOperation.getDate()).isAfterOrEqualTo(DEFAULT_DATE);
        assertThat(testAccountOperation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAccountOperation.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteAccountOperation() throws Exception {
        // Initialize the database
        accountOperationRepository.saveAndFlush(accountOperation);
        int databaseSizeBeforeDelete = accountOperationRepository.findAll().size();

        // Get the accountOperation
        restAccountOperationMockMvc.perform(delete("/api/account-operations/{id}", accountOperation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AccountOperation> accountOperations = accountOperationRepository.findAll();
        assertThat(accountOperations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
