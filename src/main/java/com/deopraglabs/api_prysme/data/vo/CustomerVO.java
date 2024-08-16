package com.deopraglabs.api_prysme.data.vo;

import com.deopraglabs.api_prysme.data.model.Address;
import com.deopraglabs.api_prysme.data.model.PhoneNumber;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private long id;
    private String cpfCnpj;
    private String name;
    private String tradeName;
    private String email;
    private LocalDate birthFoundationDate;
    private String stateRegistration;
    private List<String> phoneNumbers = new ArrayList<>();
    private Address address;
    private CartVO cart;

}
