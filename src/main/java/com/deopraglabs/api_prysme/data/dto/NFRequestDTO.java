package com.deopraglabs.api_prysme.data.dto;

import com.deopraglabs.api_prysme.data.enums.DiscountType;
import com.deopraglabs.api_prysme.data.enums.NFStatus;
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
public class NFRequestDTO {
    private String nfKey;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
    private UUID customerId;
    private UUID sellerId;
    private UUID salesOrderId;
    private List<UUID> itemIds;
    private BigDecimal totalValue;
    private BigDecimal discount;
    private DiscountType discountType;
    private NFStatus status;
    private String observations;
}