package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {

    private Long id;
    private String titulaire;
    private String email;
    private String telephone;
    private BigDecimal solde;
    private LocalDateTime dateCreation;
    private String statut;
}
