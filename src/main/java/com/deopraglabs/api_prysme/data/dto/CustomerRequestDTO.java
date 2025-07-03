package com.deopraglabs.api_prysme.data.dto;

import com.deopraglabs.api_prysme.data.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDTO {
    private String cpfCnpj;
    private String name;
    private String tradeName;
    private String email;
    private LocalDate birthFoundationDate;
    private String stateRegistration;
    private CustomerStatus customerStatus;
    private UUID sellerId;
}