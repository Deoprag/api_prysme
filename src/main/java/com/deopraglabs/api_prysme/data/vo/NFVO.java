package com.deopraglabs.api_prysme.data.vo;

import com.deopraglabs.api_prysme.data.model.DiscountType;
import com.deopraglabs.api_prysme.data.model.NFStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder("id")
public class NFVO extends RepresentationModel<NFVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private long key;
    private String nfKey;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
    private long customerId;
    private String customer;
    private long sellerId;
    private String seller;
    private SalesOrderVO salesOrder;
    private List<ItemProductVO> items;
    private BigDecimal totalValue;
    private BigDecimal discount;
    private DiscountType discountType;
    private NFStatus status;
    private String observations;
    private Date createdDate;
    private Date lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;
}
