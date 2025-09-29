package com.pismo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Request DTO for creating a new Transaction.
 */
public record CreateTransactionRequest(
        @JsonProperty("account_id")
        @NotNull(message = "Account ID is required.")
        Long accountId,

        @JsonProperty("operation_type_id")
        @NotNull(message = "Operation Type ID is required.")
        Long operationTypeId,

        // Ensure amount is non-negative for input validation. Sign logic is handled in service.
        @NotNull(message = "Amount is required.")
        @DecimalMin(value = "0.01", message = "Amount must be positive.")
        BigDecimal amount
) {
}

