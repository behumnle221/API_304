package com.bank.controller;

import com.bank.dto.AccountResponseDTO;
import com.bank.dto.CreateAccountDTO;
import com.bank.dto.SoldeResponseDTO;
import com.bank.dto.TransactionDTO;
import com.bank.dto.TransactionResponseDTO;
import com.bank.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Validated
@Tag(name = "Accounts", description = "API pour la gestion des comptes bancaires")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @Operation(summary = "Créer un nouveau compte", 
               description = "Crée un nouveau compte bancaire avec les informations fournies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Compte créé avec succès",
                    content = @Content(schema = @Schema(implementation = AccountResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "409", description = "Email déjà existant")
    })
    public ResponseEntity<AccountResponseDTO> createAccount(@Valid @RequestBody CreateAccountDTO createAccountDTO) {
        AccountResponseDTO createdAccount = accountService.createAccount(createAccountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @GetMapping
    @Operation(summary = "Lister tous les comptes", 
               description = "Récupère la liste de tous les comptes bancaires triés par date de création")
    @ApiResponse(responseCode = "200", description = "Liste des comptes récupérée avec succès",
                 content = @Content(schema = @Schema(implementation = AccountResponseDTO.class)))
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts() {
        List<AccountResponseDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/{id}/deposit")
    @Operation(summary = "Effectuer un dépôt", 
               description = "Ajoute le montant spécifié au solde du compte")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dépôt effectué avec succès",
                    content = @Content(schema = @Schema(implementation = AccountResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Montant invalide"),
        @ApiResponse(responseCode = "404", description = "Compte non trouvé")
    })
    public ResponseEntity<AccountResponseDTO> deposit(
            @PathVariable Long id, 
            @Valid @RequestBody TransactionDTO transactionDTO) {
        AccountResponseDTO account = accountService.deposit(id, transactionDTO.getMontant());
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{id}/withdraw")
    @Operation(summary = "Effectuer un retrait", 
               description = "Retire le montant spécifié du solde du compte")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retrait effectué avec succès",
                    content = @Content(schema = @Schema(implementation = AccountResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Montant invalide ou solde insuffisant"),
        @ApiResponse(responseCode = "404", description = "Compte non trouvé")
    })
    public ResponseEntity<AccountResponseDTO> withdraw(
            @PathVariable Long id, 
            @Valid @RequestBody TransactionDTO transactionDTO) {
        AccountResponseDTO account = accountService.withdraw(id, transactionDTO.getMontant());
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{id}/solde")
    @Operation(summary = "Consulter le solde", 
               description = "Récupère le solde actuel d'un compte")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solde récupéré avec succès",
                    content = @Content(schema = @Schema(implementation = SoldeResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Compte non trouvé")
    })
    public ResponseEntity<SoldeResponseDTO> getSolde(@PathVariable Long id) {
        SoldeResponseDTO solde = accountService.getSolde(id);
        return ResponseEntity.ok(solde);
    }

    @GetMapping("/{id}/transactions")
    @Operation(summary = "Historique des transactions", 
               description = "Récupère l'historique de toutes les transactions d'un compte")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historique récupéré avec succès",
                    content = @Content(schema = @Schema(implementation = TransactionResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Compte non trouvé")
    })
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionHistory(@PathVariable Long id) {
        List<TransactionResponseDTO> transactions = accountService.getTransactionHistory(id);
        return ResponseEntity.ok(transactions);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un compte", 
               description = "Supprime définitivement un compte bancaire")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Compte supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Compte non trouvé")
    })
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}