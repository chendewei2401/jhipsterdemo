package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PayRecord;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PayRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayRecordRepository extends JpaRepository<PayRecord, Long> {

}
