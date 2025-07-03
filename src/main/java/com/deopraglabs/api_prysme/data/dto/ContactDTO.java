package com.deopraglabs.api_prysme.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {
    private UUID id;
    private String name;
    private String role;
    private String department;
    private Boolean active;
    private Long customerId;
    private List<Long> contactInfoIds;
}
