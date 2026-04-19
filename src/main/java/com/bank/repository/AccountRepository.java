package com.bank.repository;

import com.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    /**
     * Recherche un compte par email
     * @param email l'email du compte
     * @return Optional contenant le compte si trouvé
     */
    Optional<Account> findByEmail(String email);
}
