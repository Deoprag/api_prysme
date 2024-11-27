package com.deopraglabs.api_prysme.data.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class ItemProductVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private long key;
    private long productId;
    private String product;
    private BigDecimal quantity;
    private long quotationId;
    private long salesOrderId;
    private long nfId;
    private BigDecimal price;
}
