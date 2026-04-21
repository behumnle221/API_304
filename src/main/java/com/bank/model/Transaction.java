package com.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    @Column(nullable = false, length = 20)
    private String statut;

    @Column(name = "date_transaction", nullable = false, updatable = false)
    private LocalDateTime dateTransaction;

    @PrePersist
    protected void onCreate() {
        dateTransaction = LocalDateTime.now();
    }

    public static final String TYPE_DEPOT = "DEPOT";
    public static final String TYPE_RETRAIT = "RETRAIT";
    public static final String STATUT_COMPLETEE = "COMPLETEE";
    public static final String STATUT_ECHEC = "ECHEC";
}