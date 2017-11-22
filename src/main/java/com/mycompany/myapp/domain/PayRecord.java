package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PayRecord.
 */
@Entity
@Table(name = "pay_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "payrecord")
public class PayRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pay_orderid")
    private String payOrderid;

    @Column(name = "order_info")
    private String orderInfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPayOrderid() {
        return payOrderid;
    }

    public PayRecord payOrderid(String payOrderid) {
        this.payOrderid = payOrderid;
        return this;
    }

    public void setPayOrderid(String payOrderid) {
        this.payOrderid = payOrderid;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public PayRecord orderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
        return this;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PayRecord payRecord = (PayRecord) o;
        if (payRecord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), payRecord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PayRecord{" +
            "id=" + getId() +
            ", payOrderid='" + getPayOrderid() + "'" +
            ", orderInfo='" + getOrderInfo() + "'" +
            "}";
    }
}
