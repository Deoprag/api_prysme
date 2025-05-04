package com.deopraglabs.api_prysme.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfoDTO {
    private Long id;
    private String email;
    private Long contactId;
    private List<Long> phoneNumberIds;
}
