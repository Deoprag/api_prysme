package com.deopraglabs.api_prysme.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryDTO {
    private UUID id;
    private String name;
    private String description;
}
