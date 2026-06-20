package com.facundo.sistemabilletera.repository;

import com.facundo.sistemabilletera.model.Wallet;
import com.facundo.sistemabilletera.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    List<WalletTransaction> findByWalletOrderByCreatedAtDesc(Wallet wallet);
}