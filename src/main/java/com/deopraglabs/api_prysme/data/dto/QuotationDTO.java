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
public class QuotationDTO {
    private Long id;
    private String quotationNumber;
    private LocalDateTime issueDate;
    private LocalDateTime validUntil;
    private BigDecimal totalValue;
    private String status;
    private UUID customerId;
    private UUID salesRepId;
    private List<Long> itemProductIds;
}
