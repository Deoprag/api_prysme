package com.deopraglabs.api_prysme.data.dto;

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
public class UserResponseDTO {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private char gender;
    private String phoneNumber;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private UUID teamId;
    private List<UUID> permissionIds;
    private Date createdDate;
    private Date lastModifiedDate;
}