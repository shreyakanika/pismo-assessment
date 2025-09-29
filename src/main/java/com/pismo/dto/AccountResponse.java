package com.pismo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response DTO for Account operations.
 */
public record AccountResponse(
        @JsonProperty("account_id")
        Long accountId,
        @JsonProperty("document_number")
        String documentNumber
) {
}
