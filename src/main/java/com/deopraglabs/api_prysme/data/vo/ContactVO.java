package com.deopraglabs.api_prysme.data.vo;

import com.deopraglabs.api_prysme.data.model.CustomerStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder("id")
public class ContactVO extends RepresentationModel<ContactVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private long key;
    private long sellerId;
    private String seller;
    private long customerId;
    private String customer;
    private ContactInfoVO info;
    private CustomerStatus customerStatus;
    private String notes;
    private LocalDateTime contactDate;
    private Date createdDate;
    private Date lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;
}
