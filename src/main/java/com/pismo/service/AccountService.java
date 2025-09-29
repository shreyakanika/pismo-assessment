package com.pismo.service;

import com.pismo.exception.ResourceNotFoundException;
import com.pismo.model.Account;
import com.pismo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Creates a new account.
     *
     * @param documentNumber The unique document number.
     * @return The saved Account entity.
     * @throws IllegalArgumentException if a documentNumber already exists.
     */
    public Account createAccount(String documentNumber) {
        try {
            Account newAccount = new Account(documentNumber);
            return accountRepository.save(newAccount);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Account with document number " + documentNumber + " already exists.");
        }
    }

    /**
     * Retrieves an account by ID.
     *
     * @param accountId The ID of the account.
     * @return The found Account entity.
     * @throws ResourceNotFoundException if the account is not found.
     */
    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for ID: " + accountId));
    }
}
