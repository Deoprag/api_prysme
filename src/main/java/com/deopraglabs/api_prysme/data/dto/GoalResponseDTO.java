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
public class GoalResponseDTO {
    private UUID id;
    private BigDecimal goal;
    private BigDecimal currentProgress;
    private UUID sellerId;
    private Date startDate;
    private Date endDate;
    private Date createdDate;
    private Date lastModifiedDate;
    private UUID createdById;
    private UUID lastModifiedById;
}