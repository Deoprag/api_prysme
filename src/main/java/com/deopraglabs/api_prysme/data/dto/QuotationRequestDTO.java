package com.deopraglabs.api_prysme.data.dto;

import com.deopraglabs.api_prysme.data.enums.QuotationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotationRequestDTO {
    private UUID customerId;
    private UUID sellerId;
    private LocalDateTime dateTime;
    private QuotationStatus quotationStatus;
    private List<UUID> itemIds;
}