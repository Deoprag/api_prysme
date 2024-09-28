package com.deopraglabs.api_prysme.data.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder("id")
public class SaleVO extends RepresentationModel<SaleVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private long key;
    private UserVO seller;
    private CustomerVO customer;
    private BigDecimal totalPrice;
    private List<ProductVO> products;
    private Date createdDate;
    private Date lastModifiedDate;
}
