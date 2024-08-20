package com.deopraglabs.api_prysme.data.vo;

import com.deopraglabs.api_prysme.data.model.ProductCategory;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal quantity;
    private ProductCategory category;
    private boolean active;

}
