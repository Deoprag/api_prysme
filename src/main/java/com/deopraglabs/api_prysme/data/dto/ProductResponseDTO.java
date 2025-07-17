package com.deopraglabs.api_prysme.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean active;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private UUID categoryId;
    private UUID createdById;
    private UUID lastModifiedById;
}