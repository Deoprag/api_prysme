package com.deopraglabs.api_prysme.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal targetValue;
    private BigDecimal currentValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private UUID userId;
}
