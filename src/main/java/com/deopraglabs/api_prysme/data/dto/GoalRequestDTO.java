package com.deopraglabs.api_prysme.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalRequestDTO {
    private BigDecimal goal;
    private UUID sellerId;
    private Date startDate;
    private Date endDate;
}