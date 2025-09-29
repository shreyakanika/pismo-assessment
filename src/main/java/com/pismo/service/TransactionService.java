package com.pismo.service;

import com.pismo.exception.ResourceNotFoundException;
import com.pismo.model.OperationType;
import com.pismo.model.Transaction;
import com.pismo.repository.AccountRepository;
import com.pismo.repository.OperationTypeRepository;
import com.pismo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private static final List<String> DEBT_OPERATIONS = List.of("PURCHASE",
            "INSTALLMENT PURCHASE", "WITHDRAWAL");
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final OperationTypeRepository operationTypeRepository;

    @Transactional
    public Transaction createTransaction(Long accountId, Long operationTypeId, BigDecimal amount) {

        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for ID: " + accountId));

        var operationType = operationTypeRepository.findById(operationTypeId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Operation Type not found for ID: " + operationTypeId));

        BigDecimal finalAmount = adjustAmountSign(operationType, amount);

        Transaction newTransaction = new Transaction();
        newTransaction.setAccount(account);
        newTransaction.setOperationType(operationType);
        newTransaction.setAmount(finalAmount);

        return transactionRepository.save(newTransaction);
    }

    /**
     * Adjusts the transaction amount based on the Operation Type.
     * Purchase, installment purchase, and withdrawal (1, 2, 3) must be negative.
     * Payment (4) must be positive.
     */
    private BigDecimal adjustAmountSign(OperationType operationType, BigDecimal inputAmount) {
        // Use the absolute value of the input amount to ensure consistency
        BigDecimal absAmount = inputAmount.abs();

        if (DEBT_OPERATIONS.contains(operationType.getDescription())) {
            return absAmount.negate();
        }
        return absAmount;
    }
}
