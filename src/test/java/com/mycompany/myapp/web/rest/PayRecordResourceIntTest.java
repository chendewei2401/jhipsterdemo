package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Demo1App;

import com.mycompany.myapp.domain.PayRecord;
import com.mycompany.myapp.repository.PayRecordRepository;
import com.mycompany.myapp.repository.search.PayRecordSearchRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PayRecordResource REST controller.
 *
 * @see PayRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Demo1App.class)
public class PayRecordResourceIntTest {

    private static final String DEFAULT_PAY_ORDERID = "AAAAAAAAAA";
    private static final String UPDATED_PAY_ORDERID = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_INFO = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_INFO = "BBBBBBBBBB";

    @Autowired
    private PayRecordRepository payRecordRepository;

    @Autowired
    private PayRecordSearchRepository payRecordSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPayRecordMockMvc;

    private PayRecord payRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PayRecordResource payRecordResource = new PayRecordResource(payRecordRepository, payRecordSearchRepository);
        this.restPayRecordMockMvc = MockMvcBuilders.standaloneSetup(payRecordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayRecord createEntity(EntityManager em) {
        PayRecord payRecord = new PayRecord()
            .payOrderid(DEFAULT_PAY_ORDERID)
            .orderInfo(DEFAULT_ORDER_INFO);
        return payRecord;
    }

    @Before
    public void initTest() {
        payRecordSearchRepository.deleteAll();
        payRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayRecord() throws Exception {
        int databaseSizeBeforeCreate = payRecordRepository.findAll().size();

        // Create the PayRecord
        restPayRecordMockMvc.perform(post("/api/pay-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payRecord)))
            .andExpect(status().isCreated());

        // Validate the PayRecord in the database
        List<PayRecord> payRecordList = payRecordRepository.findAll();
        assertThat(payRecordList).hasSize(databaseSizeBeforeCreate + 1);
        PayRecord testPayRecord = payRecordList.get(payRecordList.size() - 1);
        assertThat(testPayRecord.getPayOrderid()).isEqualTo(DEFAULT_PAY_ORDERID);
        assertThat(testPayRecord.getOrderInfo()).isEqualTo(DEFAULT_ORDER_INFO);

        // Validate the PayRecord in Elasticsearch
        PayRecord payRecordEs = payRecordSearchRepository.findOne(testPayRecord.getId());
        assertThat(payRecordEs).isEqualToComparingFieldByField(testPayRecord);
    }

    @Test
    @Transactional
    public void createPayRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = payRecordRepository.findAll().size();

        // Create the PayRecord with an existing ID
        payRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPayRecordMockMvc.perform(post("/api/pay-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payRecord)))
            .andExpect(status().isBadRequest());

        // Validate the PayRecord in the database
        List<PayRecord> payRecordList = payRecordRepository.findAll();
        assertThat(payRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPayRecords() throws Exception {
        // Initialize the database
        payRecordRepository.saveAndFlush(payRecord);

        // Get all the payRecordList
        restPayRecordMockMvc.perform(get("/api/pay-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].payOrderid").value(hasItem(DEFAULT_PAY_ORDERID.toString())))
            .andExpect(jsonPath("$.[*].orderInfo").value(hasItem(DEFAULT_ORDER_INFO.toString())));
    }

    @Test
    @Transactional
    public void getPayRecord() throws Exception {
        // Initialize the database
        payRecordRepository.saveAndFlush(payRecord);

        // Get the payRecord
        restPayRecordMockMvc.perform(get("/api/pay-records/{id}", payRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(payRecord.getId().intValue()))
            .andExpect(jsonPath("$.payOrderid").value(DEFAULT_PAY_ORDERID.toString()))
            .andExpect(jsonPath("$.orderInfo").value(DEFAULT_ORDER_INFO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPayRecord() throws Exception {
        // Get the payRecord
        restPayRecordMockMvc.perform(get("/api/pay-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayRecord() throws Exception {
        // Initialize the database
        payRecordRepository.saveAndFlush(payRecord);
        payRecordSearchRepository.save(payRecord);
        int databaseSizeBeforeUpdate = payRecordRepository.findAll().size();

        // Update the payRecord
        PayRecord updatedPayRecord = payRecordRepository.findOne(payRecord.getId());
        updatedPayRecord
            .payOrderid(UPDATED_PAY_ORDERID)
            .orderInfo(UPDATED_ORDER_INFO);

        restPayRecordMockMvc.perform(put("/api/pay-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPayRecord)))
            .andExpect(status().isOk());

        // Validate the PayRecord in the database
        List<PayRecord> payRecordList = payRecordRepository.findAll();
        assertThat(payRecordList).hasSize(databaseSizeBeforeUpdate);
        PayRecord testPayRecord = payRecordList.get(payRecordList.size() - 1);
        assertThat(testPayRecord.getPayOrderid()).isEqualTo(UPDATED_PAY_ORDERID);
        assertThat(testPayRecord.getOrderInfo()).isEqualTo(UPDATED_ORDER_INFO);

        // Validate the PayRecord in Elasticsearch
        PayRecord payRecordEs = payRecordSearchRepository.findOne(testPayRecord.getId());
        assertThat(payRecordEs).isEqualToComparingFieldByField(testPayRecord);
    }

    @Test
    @Transactional
    public void updateNonExistingPayRecord() throws Exception {
        int databaseSizeBeforeUpdate = payRecordRepository.findAll().size();

        // Create the PayRecord

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPayRecordMockMvc.perform(put("/api/pay-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payRecord)))
            .andExpect(status().isCreated());

        // Validate the PayRecord in the database
        List<PayRecord> payRecordList = payRecordRepository.findAll();
        assertThat(payRecordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePayRecord() throws Exception {
        // Initialize the database
        payRecordRepository.saveAndFlush(payRecord);
        payRecordSearchRepository.save(payRecord);
        int databaseSizeBeforeDelete = payRecordRepository.findAll().size();

        // Get the payRecord
        restPayRecordMockMvc.perform(delete("/api/pay-records/{id}", payRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean payRecordExistsInEs = payRecordSearchRepository.exists(payRecord.getId());
        assertThat(payRecordExistsInEs).isFalse();

        // Validate the database is empty
        List<PayRecord> payRecordList = payRecordRepository.findAll();
        assertThat(payRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPayRecord() throws Exception {
        // Initialize the database
        payRecordRepository.saveAndFlush(payRecord);
        payRecordSearchRepository.save(payRecord);

        // Search the payRecord
        restPayRecordMockMvc.perform(get("/api/_search/pay-records?query=id:" + payRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].payOrderid").value(hasItem(DEFAULT_PAY_ORDERID.toString())))
            .andExpect(jsonPath("$.[*].orderInfo").value(hasItem(DEFAULT_ORDER_INFO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayRecord.class);
        PayRecord payRecord1 = new PayRecord();
        payRecord1.setId(1L);
        PayRecord payRecord2 = new PayRecord();
        payRecord2.setId(payRecord1.getId());
        assertThat(payRecord1).isEqualTo(payRecord2);
        payRecord2.setId(2L);
        assertThat(payRecord1).isNotEqualTo(payRecord2);
        payRecord1.setId(null);
        assertThat(payRecord1).isNotEqualTo(payRecord2);
    }
}
