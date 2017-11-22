package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.PayRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PayRecord entity.
 */
public interface PayRecordSearchRepository extends ElasticsearchRepository<PayRecord, Long> {
}
