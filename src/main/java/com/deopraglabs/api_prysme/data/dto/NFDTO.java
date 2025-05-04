package com.deopraglabs.api_prysme.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NFDTO {
    private Long id;
    private String nfKey;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
    private UUID customerId;
    private UUID sellerId;
    private Long salesOrderId;
    private List<Long> itemProductIds;
    private BigDecimal totalValue;
    private BigDecimal discount;
    private String discountType;
    private String status;
    private String observations;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
