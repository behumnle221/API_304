package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoldeResponseDTO {

    private Long accountId;
    private String titulaire;
    private BigDecimal solde;
    private String statut;
}