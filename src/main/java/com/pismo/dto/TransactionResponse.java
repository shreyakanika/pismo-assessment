package com.pismo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for Transaction operations.
 */
public record TransactionResponse(
        @JsonProperty("transaction_id")
        Long transactionId,
        @JsonProperty("account_id")
        Long accountId,
        @JsonProperty("operation_type_id")
        Long operationTypeId,
        BigDecimal amount,
        @JsonProperty("event_date")
        LocalDateTime eventDate
) {
}