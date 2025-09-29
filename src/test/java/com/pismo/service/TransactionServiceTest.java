package com.pismo.service;

import com.pismo.exception.ResourceNotFoundException;
import com.pismo.model.Account;
import com.pismo.model.OperationType;
import com.pismo.model.Transaction;
import com.pismo.repository.AccountRepository;
import com.pismo.repository.OperationTypeRepository;
import com.pismo.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private OperationTypeRepository operationTypeRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Account mockAccount;
    private OperationType purchaseType;
    private OperationType paymentType;

    @BeforeEach
    void setUp() {
        mockAccount = new Account("12345678900");
        mockAccount.setId(1L);

        purchaseType = new OperationType();
        purchaseType.setId(1L);
        purchaseType.setDescription("PURCHASE");

        paymentType = new OperationType();
        paymentType.setId(4L);
        paymentType.setDescription("PAYMENT");
    }

    @Test
    void createTransaction_ShouldThrowNotFound_WhenAccountDoesNotExist() {
        // Arrange
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.createTransaction(99L, 1L, BigDecimal.TEN));
        verify(operationTypeRepository, never()).findById(anyLong());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void createTransaction_ShouldThrowNotFound_WhenOperationTypeDoesNotExist() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.of(mockAccount));
        when(operationTypeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.createTransaction(1L, 99L, BigDecimal.TEN));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void createTransaction_DebtType_ShouldStoreNegativeAmount() {
        // Arrange
        BigDecimal inputAmount = new BigDecimal("50.00");
        BigDecimal expectedAmount = new BigDecimal("-50.00");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(mockAccount));
        when(operationTypeRepository.findById(1L)).thenReturn(Optional.of(purchaseType));

        // Use `any()` for the Transaction save argument to capture and assert the internal state
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction t = invocation.getArgument(0);
            t.setId(1L);
            // Verify the crucial business logic: amount sign
            assertEquals(expectedAmount, t.getAmount());
            return t;
        });

        // Act
        Transaction result = transactionService.createTransaction(1L, 1L, inputAmount);

        // Assert
        assertNotNull(result);
        assertEquals(expectedAmount, result.getAmount());
    }

    @Test
    void createTransaction_PaymentType_ShouldStorePositiveAmount() {
        // Arrange
        BigDecimal inputAmount = new BigDecimal("100.00");
        BigDecimal expectedAmount = new BigDecimal("100.00");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(mockAccount));
        when(operationTypeRepository.findById(4L)).thenReturn(Optional.of(paymentType));

        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction t = invocation.getArgument(0);
            t.setId(2L);
            // Verify the crucial business logic: amount sign
            assertEquals(expectedAmount, t.getAmount());
            return t;
        });

        // Act
        Transaction result = transactionService.createTransaction(1L, 4L, inputAmount);

        // Assert
        assertNotNull(result);
        assertEquals(expectedAmount, result.getAmount());
    }
}
