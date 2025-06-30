package com.deopraglabs.api_prysme.data.dto;

import com.deopraglabs.api_prysme.data.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private UUID id;
    private String cpfCnpj;
    private String name;
    private String tradeName;
    private String email;
    private LocalDate birthFoundationDate;
    private String stateRegistration;
    private CustomerStatus customerStatus;
    private List<UUID> phoneNumberIds;
    private UUID addressId;
    private UUID sellerId;
    private Date createdDate;
    private Date lastModifiedDate;
    private UUID createdById;
    private UUID lastModifiedById;
}