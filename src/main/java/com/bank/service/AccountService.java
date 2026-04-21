package com.bank.service;

import com.bank.dto.AccountResponseDTO;
import com.bank.dto.CreateAccountDTO;
import com.bank.dto.SoldeResponseDTO;
import com.bank.dto.TransactionResponseDTO;
import com.bank.exception.AccountAlreadyExistsException;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientFundsException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

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

    public AccountResponseDTO deposit(Long accountId, BigDecimal montant) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Compte non trouvé avec l'id: " + accountId));
        
        account.setSolde(account.getSolde().add(montant));
        Account updatedAccount = accountRepository.save(account);
        
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(Transaction.TYPE_DEPOT);
        transaction.setMontant(montant);
        transaction.setStatut(Transaction.STATUT_COMPLETEE);
        transactionRepository.save(transaction);
        
        return convertToDTO(updatedAccount);
    }

    public AccountResponseDTO withdraw(Long accountId, BigDecimal montant) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Compte non trouvé avec l'id: " + accountId));
        
        if (account.getSolde().compareTo(montant) < 0) {
            Transaction transaction = new Transaction();
            transaction.setAccount(account);
            transaction.setType(Transaction.TYPE_RETRAIT);
            transaction.setMontant(montant);
            transaction.setStatut(Transaction.STATUT_ECHEC);
            transactionRepository.save(transaction);
            
            throw new InsufficientFundsException("Solde insuffisant pour effectuer ce retrait");
        }
        
        account.setSolde(account.getSolde().subtract(montant));
        Account updatedAccount = accountRepository.save(account);
        
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(Transaction.TYPE_RETRAIT);
        transaction.setMontant(montant);
        transaction.setStatut(Transaction.STATUT_COMPLETEE);
        transactionRepository.save(transaction);
        
        return convertToDTO(updatedAccount);
    }

    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll()
            .stream()
            .sorted((a1, a2) -> a2.getDateCreation().compareTo(a1.getDateCreation()))
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public SoldeResponseDTO getSolde(Long accountId) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Compte non trouvé avec l'id: " + accountId));
        
        return new SoldeResponseDTO(
            account.getId(),
            account.getTitulaire(),
            account.getSolde(),
            account.getStatut()
        );
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getTransactionHistory(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException("Compte non trouvé avec l'id: " + accountId);
        }
        
        return transactionRepository.findByAccountIdOrderByDateTransactionDesc(accountId)
            .stream()
            .map(this::convertTransactionToDTO)
            .collect(Collectors.toList());
    }

    public void deleteAccount(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException("Compte non trouvé avec l'id: " + accountId);
        }
        accountRepository.deleteById(accountId);
    }

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

    private TransactionResponseDTO convertTransactionToDTO(Transaction transaction) {
        return new TransactionResponseDTO(
            transaction.getId(),
            transaction.getAccount().getId(),
            transaction.getType(),
            transaction.getMontant(),
            transaction.getStatut(),
            transaction.getDateTransaction()
        );
    }
}