package com.deopraglabs.api_prysme.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private char gender;
    private String phoneNumber;
    private UUID teamId;
    private List<UUID> permissionIds;
}