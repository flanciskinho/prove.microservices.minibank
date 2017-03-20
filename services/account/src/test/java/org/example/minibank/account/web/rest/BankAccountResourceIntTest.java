package org.example.minibank.account.web.rest;

import org.example.minibank.account.AccountApp;

import org.example.minibank.account.config.JHipsterProperties;
import org.example.minibank.account.domain.BankAccount;
import org.example.minibank.account.repository.BankAccountRepository;
import org.example.minibank.account.security.SecurityUtilTest;
import org.example.minibank.account.service.BankAccountService;
import org.example.minibank.account.service.dto.AmountDTO;
import org.example.minibank.account.service.dto.BankAccountDTO;
import org.example.minibank.account.service.mapper.BankAccountMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BankAccountResource REST controller.
 *
 * @see BankAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountApp.class)
public class BankAccountResourceIntTest {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private static final Long DEFAULT_USER_ID = 1L;

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);

    @Inject
    private BankAccountRepository bankAccountRepository;

    @Inject
    private BankAccountMapper bankAccountMapper;

    @Inject
    private BankAccountService bankAccountService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBankAccountMockMvc;

    private BankAccount bankAccount;

    private ConfigurableApplicationContext configurableApplicationContext;
    @BeforeTransaction
    private void startAppForMicroservices() {
        String args[] = {};
        configurableApplicationContext = new SpringApplicationBuilder(AccountApp.class).web(true).run(args);
    }
    @AfterTransaction
    private void endAppForMicroservices() {
        configurableApplicationContext.close();
    }

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BankAccountResource bankAccountResource = new BankAccountResource();
        ReflectionTestUtils.setField(bankAccountResource, "bankAccountService", bankAccountService);
        this.restBankAccountMockMvc = MockMvcBuilders.standaloneSetup(bankAccountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankAccount createEntity(EntityManager em) {
        BankAccount bankAccount = new BankAccount()
                .userId(DEFAULT_USER_ID)
                .balance(DEFAULT_BALANCE);
        return bankAccount;
    }

    @Before
    public void initTest() {
        bankAccount = createEntity(em);
        SecurityUtilTest.authenticate(DEFAULT_USER_ID, "user", SecurityUtilTest.USER_ROLE);
    }

    @Test
    @Transactional
    public void createBankAccount() throws Exception {
        int databaseSizeBeforeCreate = bankAccountRepository.findAll().size();

        // Create the BankAccount
        BankAccountDTO bankAccountDTO = bankAccountMapper.bankAccountToBankAccountDTO(bankAccount);

        restBankAccountMockMvc.perform(post("/api/bank-accounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO)))
                .andExpect(status().isCreated());

        // Validate the BankAccount in the database
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        assertThat(bankAccounts).hasSize(databaseSizeBeforeCreate + 1);
        BankAccount testBankAccount = bankAccounts.get(bankAccounts.size() - 1);
        assertThat(testBankAccount.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testBankAccount.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankAccountRepository.findAll().size();
        // set the field null
        bankAccount.setUserId(null);

        // Create the BankAccount, which fails.
        BankAccountDTO bankAccountDTO = bankAccountMapper.bankAccountToBankAccountDTO(bankAccount);

        restBankAccountMockMvc.perform(post("/api/bank-accounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO)))
                .andExpect(status().isBadRequest());

        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        assertThat(bankAccounts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankAccountRepository.findAll().size();
        // set the field null
        bankAccount.setBalance(null);

        // Create the BankAccount, which fails.
        BankAccountDTO bankAccountDTO = bankAccountMapper.bankAccountToBankAccountDTO(bankAccount);

        restBankAccountMockMvc.perform(post("/api/bank-accounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bankAccountDTO)))
                .andExpect(status().isBadRequest());

        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        assertThat(bankAccounts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBankAccounts() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get all the bankAccounts
        restBankAccountMockMvc.perform(get("/api/bank-accounts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bankAccount.getId().intValue())))
                .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
                .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));
    }

    @Test
    @Transactional
    public void getBankAccount() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);

        // Get the bankAccount
        restBankAccountMockMvc.perform(get("/api/bank-accounts/{id}", bankAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bankAccount.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBankAccount() throws Exception {
        // Get the bankAccount
        restBankAccountMockMvc.perform(get("/api/bank-accounts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteBankAccount() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);
        int databaseSizeBeforeDelete = bankAccountRepository.findAll().size();

        // Get the bankAccount
        restBankAccountMockMvc.perform(delete("/api/bank-accounts/{id}", bankAccount.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        assertThat(bankAccounts).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void addAmount() throws Exception {
        // Initialize the database
        bankAccountRepository.saveAndFlush(bankAccount);
        int databaseSizeBefore = bankAccountRepository.findAll().size();

        BigDecimal amount = new BigDecimal(1000);
        AmountDTO amountDTO = new AmountDTO(bankAccount.getId(), amount);

        // Do the operation
        restBankAccountMockMvc.perform(post("/api/bank-accounts/add")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amountDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(bankAccount.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()+amount.intValue()));

        // Validate the database has the same size
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        assertThat(bankAccounts).hasSize(databaseSizeBefore);
    }

    @Test
    @Transactional
    public void withDrawAmount() throws Exception {
        // Initialize the database
        BigDecimal initialAmount = new BigDecimal(1000);
        bankAccount.setBalance(initialAmount);
        bankAccountRepository.saveAndFlush(bankAccount);
        int databaseSizeBefore = bankAccountRepository.findAll().size();

        BigDecimal amount = initialAmount.divide(new BigDecimal(5));
        AmountDTO amountDTO = new AmountDTO(bankAccount.getId(), amount);

        // Do the operation
        restBankAccountMockMvc.perform(post("/api/bank-accounts/withdraw")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(amountDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(bankAccount.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.balance").value(initialAmount.subtract(amount).intValue()));

        // Validate the database has the same size
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        assertThat(bankAccounts).hasSize(databaseSizeBefore);
    }
}
