package com.deopraglabs.api_prysme.data.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder("id")
public class AddressVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private long key;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    @JsonProperty("postal_code")
    private String postalCode;
    private String country;
}