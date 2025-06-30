package com.deopraglabs.api_prysme.data.dto;

import com.deopraglabs.api_prysme.data.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderRequestDTO {
    private UUID quotationId;
    private UUID customerId;
    private UUID sellerId;
    private OrderStatus status;
    private String notes;
    private List<UUID> itemIds;
}