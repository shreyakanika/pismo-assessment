package com.pismo.controller;

import com.pismo.dto.AccountResponse;
import com.pismo.dto.CreateAccountRequest;
import com.pismo.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Operations related to customer accounts.")
@Validated
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @Operation(summary = "Creates a new account",
            description = "The request body receives a document number that uniquely identifies the account owner.")
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {

        var createdAccount = accountService.createAccount(request.documentNumber());

        var response = new AccountResponse(
                createdAccount.getId(),
                createdAccount.getDocumentNumber()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Retrieves an existing account by ID",
            description = "Returns the account details if the ID is valid. Returns 404 if the account is not found.")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId) {

        // ResourceNotFoundException is handled by ApiExceptionHandler and mapped to 404.
        var account = accountService.getAccount(accountId);

        var response = new AccountResponse(
                account.getId(),
                account.getDocumentNumber()
        );

        return ResponseEntity.ok(response);
    }
}
