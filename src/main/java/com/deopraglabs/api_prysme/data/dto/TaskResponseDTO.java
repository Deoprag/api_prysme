package com.deopraglabs.api_prysme.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private Date dueDate;
    private UUID assignedToId;
    private Date createdDate;
    private Date lastModifiedDate;
    private UUID createdById;
    private UUID lastModifiedById;
}