package com.deopraglabs.api_prysme.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private UUID id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String priority;
    private String status;
    private UUID assignedToId;
    private UUID createdById;
}
