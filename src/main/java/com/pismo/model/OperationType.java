package com.pismo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "operation_type")
public class OperationType {

    @Id
    private Long id;

    // The Description (e.g., PURCHASE, PAYMENT)
    private String description;
}
