package com.pismo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pismo.dto.CreateAccountRequest;
import com.pismo.model.Account;
import com.pismo.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Ensures the database state is rolled back after each test
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void postAccount_ShouldCreateAccount_AndReturn201() throws Exception {
        // Arrange
        String docNumber = "99988877766";
        CreateAccountRequest request = new CreateAccountRequest(docNumber);

        // Act & Assert
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.document_number", is(docNumber)))
                .andExpect(jsonPath("$.account_id").isNumber());
    }

    @Test
    void postAccount_ShouldReturn400_WhenDocumentNumberIsMissing() throws Exception {
        // Arrange (Request with null document number, violating DTO validation)
        String invalidRequest = "{\"document_number\": null}";

        // Act & Assert
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postAccount_ShouldReturn400_WhenDocumentNumberIsDuplicate() throws Exception {
        // Arrange: Pre-save an account
        String docNumber = "11122233300";
        accountRepository.save(new Account(docNumber));
        CreateAccountRequest request = new CreateAccountRequest(docNumber);

        // Act & Assert: Attempt to save the same document number again
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // Handled by DataIntegrityViolation/ApiExceptionHandler
    }

    @Test
    void getAccount_ShouldReturnAccount_AndReturn200() throws Exception {
        // Arrange: Pre-save an account
        Account existingAccount = accountRepository.save(new Account("54321098765"));

        // Act & Assert
        mockMvc.perform(get("/accounts/{accountId}", existingAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account_id", is(existingAccount.getId().intValue())))
                .andExpect(jsonPath("$.document_number", is("54321098765")));
    }

    @Test
    void getAccount_ShouldReturn404_WhenAccountDoesNotExist() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/accounts/99999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // Handled by ResourceNotFoundException
                .andExpect(jsonPath("$.error", is("Not Found")));
    }
}
