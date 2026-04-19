package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountDTO {

    @NotBlank(message = "Le titulaire ne peut pas être vide")
    @Size(min = 3, max = 100, message = "Le titulaire doit contenir entre 3 et 100 caractères")
    private String titulaire;

    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "L'email doit être valide")
    @Size(min = 5, max = 100, message = "L'email doit contenir entre 5 et 100 caractères")
    private String email;

    @NotBlank(message = "Le téléphone ne peut pas être vide")
    @Pattern(regexp = "^[0-9]{8,}$", message = "Le téléphone doit contenir au minimum 8 chiffres")
    private String telephone;

    @NotNull(message = "Le solde ne peut pas être vide")
    @DecimalMin(value = "0.0", inclusive = true, message = "Le solde doit être >= 0")
    private BigDecimal solde;
}
