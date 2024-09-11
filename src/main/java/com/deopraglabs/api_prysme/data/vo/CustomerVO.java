package com.deopraglabs.api_prysme.data.vo;

import com.deopraglabs.api_prysme.data.model.CustomerStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder({"id", "cpf_cnpj", "name", "trade_name", "email", "birth_foundation_date", "state_registration", "customer_status", "phone_numbers", "address", "cart"})
public class CustomerVO extends RepresentationModel<CustomerVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private long key;
    @JsonProperty("cpf_cnpj")
    private String cpfCnpj;
    private String name;
    @JsonProperty("trade_name")
    private String tradeName;
    private String email;
    @JsonProperty("birth_foundation_date")
    private LocalDate birthFoundationDate;
    @JsonProperty("state_registration")
    private String stateRegistration;
    @JsonProperty("customer_status")
    private CustomerStatus customerStatus;
    @JsonProperty("phone_numbers")
    private List<String> phoneNumbers = new ArrayList<>();
    private AddressVO address;
    private CartVO cart;

}
