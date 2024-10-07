package com.deopraglabs.api_prysme.data.vo;

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

    private long id;
    private ProductVO product;
    private BigDecimal quantity;
    private QuotationVO quotation;
    private SalesOrderVO salesOrder;
    private NFVO nf;
    private BigDecimal price;
}
