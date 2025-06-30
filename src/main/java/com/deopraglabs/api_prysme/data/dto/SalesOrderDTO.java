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
public class SalesOrderDTO {
    private UUID id;
    private String orderNumber;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private BigDecimal totalValue;
    private String status;
    private UUID customerId;
    private UUID salesRepId;
    private Long quotationId;
    private List<Long> itemProductIds;
    private List<Long> nfIds;
}
