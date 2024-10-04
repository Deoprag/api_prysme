package com.deopraglabs.api_prysme.data.vo;

import com.deopraglabs.api_prysme.data.model.ItemProduct;
import com.deopraglabs.api_prysme.data.model.QuotationStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder("id")
public class QuotationVO extends RepresentationModel<QuotationVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private long key;
    private long customerId;
    private String customerName;
    private long sellerId;
    private String sellerName;
    private LocalDateTime dateTime;
    private QuotationStatus quotationStatus;
    private List<ItemProduct> items;
    private Date createdDate;
    private Date lastModifiedDate;
}
