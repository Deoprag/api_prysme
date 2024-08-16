package com.deopraglabs.api_prysme.data.vo;

import com.deopraglabs.api_prysme.data.model.Product;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class CartVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private long id;
    private long customerId;
    private String customer;
    private List<Product> products;
    private double totalPrice;

}
