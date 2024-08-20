package com.deopraglabs.api_prysme.data.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SaleVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private long id;
    private UserVO seller;
    private CustomerVO customer;
    private BigDecimal totalPrice;
    private List<ProductVO> products;

}
