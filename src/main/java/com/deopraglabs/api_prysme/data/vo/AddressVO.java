package com.deopraglabs.api_prysme.data.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AddressVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private long id;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}