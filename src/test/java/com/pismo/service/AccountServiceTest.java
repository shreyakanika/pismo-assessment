package com.pismo.service;

import com.pismo.exception.ResourceNotFoundException;
import com.pismo.model.Account;
import com.pismo.model.OperationType;
import com.pismo.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    // Inject the mock dependencies into the AccountService instance being tested
    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    private static final String VALID_DOCUMENT = "12345678900";
    private static final Long ACCOUNT_ID = 1L;

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
    void createAccount_shouldReturnSavedAccount_onSuccess() {
        when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);

        // Perform Call
        Account result = accountService.createAccount(VALID_DOCUMENT);

        // Assertions
        assertNotNull(result, "The created account should not be null.");
        assertEquals(ACCOUNT_ID, result.getId(), "The returned account should have the generated ID.");
        assertEquals(VALID_DOCUMENT, result.getDocumentNumber(), "The document number should match the input.");

        // Verification
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createAccount_shouldThrowIllegalArgumentException_whenDocumentExists() {

        // When save is called, simulate a database constraint violation exception
        doThrow(DataIntegrityViolationException.class)
                .when(accountRepository).save(any(Account.class));

        // Verify that the specific exception is thrown
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createAccount(VALID_DOCUMENT);
        });

        // Verify the exception message is correct
        String expectedMessage = "Account with document number " + VALID_DOCUMENT + " already exists.";
        assertEquals(expectedMessage, exception.getMessage());

        // Verification
        verify(accountRepository, times(1)).save(any(Account.class));
    }


    @Test
    void getAccount_shouldReturnAccount_whenFound() {
        // When findById is called with the ID, return the account wrapped in an Optional
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(mockAccount));

        // Perform Call
        Account result = accountService.getAccount(ACCOUNT_ID);

        // Assertions
        assertNotNull(result, "The found account should not be null.");
        assertEquals(ACCOUNT_ID, result.getId(), "The returned account ID should match the request ID.");

        // Verification
        verify(accountRepository, times(1)).findById(ACCOUNT_ID);
    }

    @Test
    void getAccount_shouldThrowResourceNotFoundException_whenNotFound() throws Exception{
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        // Verify that the ResourceNotFoundException is thrown
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccount(ACCOUNT_ID);
        });
        // Verify the exception message is correct
        String expectedMessage = "Account not found for ID: " + ACCOUNT_ID;
        assertEquals(expectedMessage, exception.getMessage());

        //Verification
        verify(accountRepository, times(1)).findById(ACCOUNT_ID);
    }
}
