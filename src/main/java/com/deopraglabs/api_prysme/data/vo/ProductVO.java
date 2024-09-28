package com.deopraglabs.api_prysme.data.vo;

import com.deopraglabs.api_prysme.data.model.ProductCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder("id")
public class ProductVO extends RepresentationModel<ProductVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private long key;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal stock;
    private long categoryId;
    private String categoryName;
    private boolean active;
    private Date createdDate;
    private Date lastModifiedDate;
}
