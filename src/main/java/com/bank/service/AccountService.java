package com.bank.service;

import com.bank.dto.AccountResponseDTO;
import com.bank.dto.CreateAccountDTO;
import com.bank.exception.AccountAlreadyExistsException;
import com.bank.exception.InsufficientFundsException;
import com.bank.model.Account;
import com.bank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Crée un nouveau compte bancaire
     * @param createAccountDTO les informations du compte à créer
     * @return le compte créé
     * @throws AccountAlreadyExistsException si l'email existe déjà
     */
    public AccountResponseDTO createAccount(CreateAccountDTO createAccountDTO) {
        if (accountRepository.findByEmail(createAccountDTO.getEmail()).isPresent()) {
            throw new AccountAlreadyExistsException(
                "L'email " + createAccountDTO.getEmail() + " est déjà utilisé"
            );
        }

        Account account = new Account();
        account.setTitulaire(createAccountDTO.getTitulaire());
        account.setEmail(createAccountDTO.getEmail());
        account.setTelephone(createAccountDTO.getTelephone());
        account.setSolde(createAccountDTO.getSolde());

        Account savedAccount = accountRepository.save(account);

        return convertToDTO(savedAccount);
    }

    public AccountResponseDTO deposit(Long accountId, java.math.BigDecimal montant) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Compte non trouvé avec l'id: " + accountId));
        
        account.setSolde(account.getSolde().add(montant));
        Account updatedAccount = accountRepository.save(account);
        
        return convertToDTO(updatedAccount);
    }

    public AccountResponseDTO withdraw(Long accountId, java.math.BigDecimal montant) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Compte non trouvé avec l'id: " + accountId));
        
        if (account.getSolde().compareTo(montant) < 0) {
            throw new InsufficientFundsException("Solde insuffisant pour effectuer ce retrait");
        }
        
        account.setSolde(account.getSolde().subtract(montant));
        Account updatedAccount = accountRepository.save(account);
        
        return convertToDTO(updatedAccount);
    }

    /**
     * Récupère la liste de tous les comptes
     * @return liste des comptes triée par date de création (récent d'abord)
     */
    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll()
            .stream()
            .sorted((a1, a2) -> a2.getDateCreation().compareTo(a1.getDateCreation()))
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Convertit une entité Account en DTO
     * @param account l'entité Account
     * @return le DTO correspondant
     */
    private AccountResponseDTO convertToDTO(Account account) {
        return new AccountResponseDTO(
            account.getId(),
            account.getTitulaire(),
            account.getEmail(),
            account.getTelephone(),
            account.getSolde(),
            account.getDateCreation(),
            account.getStatut()
        );
    }
}
