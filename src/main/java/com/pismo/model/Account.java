package com.pismo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {

    // Account_ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Document_Number (The unique identifier for the account owner)
    // The 'unique = true' constraint ensures that no two accounts can have the same document number.
    @Column(name = "document_number", unique = true, nullable = false)
    private String documentNumber;

    // Constructor used when creating a new account from the API request
    public Account(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}
