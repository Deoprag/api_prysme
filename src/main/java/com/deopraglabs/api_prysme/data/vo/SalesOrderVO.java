package com.deopraglabs.api_prysme.data.vo;

import com.deopraglabs.api_prysme.data.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder("id")
public class SalesOrderVO extends RepresentationModel<SalesOrderVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private long key;
    private long quotationId;
    private long customerId;
    private String customer;
    private long sellerId;
    private String seller;
    private Date dateTime;
    private String notes;
    private OrderStatus status;
    private List<ItemProductVO> items;
    private Date createdDate;
    private Date lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;
}
