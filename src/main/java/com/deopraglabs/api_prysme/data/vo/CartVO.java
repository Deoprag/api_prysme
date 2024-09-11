package com.deopraglabs.api_prysme.data.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@JsonPropertyOrder("id")
public class CartVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private long key;
    private long customerId;
    private String customer;
    private List<ProductVO> products;
    private double totalPrice;

}
