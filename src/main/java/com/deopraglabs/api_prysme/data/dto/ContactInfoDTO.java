package com.deopraglabs.api_prysme.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfoDTO {
    private UUID id;
    private String email;
    private UUID contactId;
    private List<UUID> phoneNumberIds;
}
