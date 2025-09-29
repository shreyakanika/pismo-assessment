package com.pismo.controller;

import com.pismo.dto.CreateTransactionRequest;
import com.pismo.dto.TransactionResponse;
import com.pismo.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Operations related to transactions (purchases, payments, withdrawals).")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Creates a new transaction for an account",
            description = "Requires account_id, operation_type_id, and amount. Performs validation on IDs and adjusts amount sign based on operation type.")
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody CreateTransactionRequest request) {

        var createdTransaction = transactionService.createTransaction(
                request.accountId(),
                request.operationTypeId(),
                request.amount()
        );

        // The response body should return the transaction object if successful (201 Created).
        var response = new TransactionResponse(
                createdTransaction.getId(),
                createdTransaction.getAccount().getId(),
                createdTransaction.getOperationType().getId(),
                createdTransaction.getAmount(),
                createdTransaction.getEventDate()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
