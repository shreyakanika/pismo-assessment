package com.pismo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Request DTO for creating a new Account.
 * Maps 'document_number' from JSON snake_case.
 */
public record CreateAccountRequest(
        @JsonProperty("document_number")
        @NotBlank(message = "Document number is required.")
        @Pattern(regexp="^(0|[1-9][0-9]*)$",message = "Document number must contains digit only.")
        String documentNumber
) {
}
