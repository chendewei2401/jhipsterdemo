package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.PayRecord;

import com.mycompany.myapp.repository.PayRecordRepository;
import com.mycompany.myapp.repository.search.PayRecordSearchRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing PayRecord.
 */
@RestController
@RequestMapping("/api")
public class PayRecordResource {

    private final Logger log = LoggerFactory.getLogger(PayRecordResource.class);

    private static final String ENTITY_NAME = "payRecord";

    private final PayRecordRepository payRecordRepository;

    private final PayRecordSearchRepository payRecordSearchRepository;

    public PayRecordResource(PayRecordRepository payRecordRepository, PayRecordSearchRepository payRecordSearchRepository) {
        this.payRecordRepository = payRecordRepository;
        this.payRecordSearchRepository = payRecordSearchRepository;
    }

    /**
     * POST  /pay-records : Create a new payRecord.
     *
     * @param payRecord the payRecord to create
     * @return the ResponseEntity with status 201 (Created) and with body the new payRecord, or with status 400 (Bad Request) if the payRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pay-records")
    @Timed
    public ResponseEntity<PayRecord> createPayRecord(@RequestBody PayRecord payRecord) throws URISyntaxException {
        log.debug("REST request to save PayRecord : {}", payRecord);
        if (payRecord.getId() != null) {
            throw new BadRequestAlertException("A new payRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PayRecord result = payRecordRepository.save(payRecord);
        payRecordSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pay-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pay-records : Updates an existing payRecord.
     *
     * @param payRecord the payRecord to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated payRecord,
     * or with status 400 (Bad Request) if the payRecord is not valid,
     * or with status 500 (Internal Server Error) if the payRecord couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pay-records")
    @Timed
    public ResponseEntity<PayRecord> updatePayRecord(@RequestBody PayRecord payRecord) throws URISyntaxException {
        log.debug("REST request to update PayRecord : {}", payRecord);
        if (payRecord.getId() == null) {
            return createPayRecord(payRecord);
        }
        PayRecord result = payRecordRepository.save(payRecord);
        payRecordSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, payRecord.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pay-records : get all the payRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of payRecords in body
     */
    @GetMapping("/pay-records")
    @Timed
    public ResponseEntity<List<PayRecord>> getAllPayRecords(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of PayRecords");
        Page<PayRecord> page = payRecordRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pay-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pay-records/:id : get the "id" payRecord.
     *
     * @param id the id of the payRecord to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the payRecord, or with status 404 (Not Found)
     */
    @GetMapping("/pay-records/{id}")
    @Timed
    public ResponseEntity<PayRecord> getPayRecord(@PathVariable Long id) {
        log.debug("REST request to get PayRecord : {}", id);
        PayRecord payRecord = payRecordRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(payRecord));
    }

    /**
     * DELETE  /pay-records/:id : delete the "id" payRecord.
     *
     * @param id the id of the payRecord to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pay-records/{id}")
    @Timed
    public ResponseEntity<Void> deletePayRecord(@PathVariable Long id) {
        log.debug("REST request to delete PayRecord : {}", id);
        payRecordRepository.delete(id);
        payRecordSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/pay-records?query=:query : search for the payRecord corresponding
     * to the query.
     *
     * @param query the query of the payRecord search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/pay-records")
    @Timed
    public ResponseEntity<List<PayRecord>> searchPayRecords(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of PayRecords for query {}", query);
        Page<PayRecord> page = payRecordSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/pay-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
