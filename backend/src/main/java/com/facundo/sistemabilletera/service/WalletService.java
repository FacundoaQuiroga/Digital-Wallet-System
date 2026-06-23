package com.facundo.sistemabilletera.service;
import com.facundo.sistemabilletera.model.Wallet;
import com.facundo.sistemabilletera.model.WalletTransaction;
import com.facundo.sistemabilletera.repository.WalletRepository;
import com.facundo.sistemabilletera.repository.WalletTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import java.math.BigDecimal;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public WalletService(
            WalletRepository walletRepository,
            WalletTransactionRepository walletTransactionRepository
    ) {
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    @Transactional
    public Wallet deposit(Long walletId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        wallet.setBalance(wallet.getBalance().add(amount));
        Wallet savedWallet = walletRepository.save(wallet);

        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(savedWallet);
        transaction.setType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setDescription("Wallet deposit");

        walletTransactionRepository.save(transaction);

        return savedWallet;
    }

    @Transactional
    public void transfer(Long senderWalletId, Long receiverWalletId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (senderWalletId.equals(receiverWalletId)) {
            throw new IllegalArgumentException("Cannot transfer to the same wallet");
        }

        Wallet senderWallet = walletRepository.findById(senderWalletId)
                .orElseThrow(() -> new IllegalArgumentException("Sender wallet not found"));

        Wallet receiverWallet = walletRepository.findById(receiverWalletId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver wallet not found"));

        if (senderWallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(amount));
        receiverWallet.setBalance(receiverWallet.getBalance().add(amount));

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        WalletTransaction sentTransaction = new WalletTransaction();
        sentTransaction.setWallet(senderWallet);
        sentTransaction.setType("TRANSFER_SENT");
        sentTransaction.setAmount(amount);
        sentTransaction.setDescription("Transfer sent to wallet " + receiverWalletId);

        WalletTransaction receivedTransaction = new WalletTransaction();
        receivedTransaction.setWallet(receiverWallet);
        receivedTransaction.setType("TRANSFER_RECEIVED");
        receivedTransaction.setAmount(amount);
        receivedTransaction.setDescription("Transfer received from wallet " + senderWalletId);

        walletTransactionRepository.save(sentTransaction);
        walletTransactionRepository.save(receivedTransaction);
    }
    

    public List<WalletTransaction> getTransactions(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        return walletTransactionRepository.findByWalletOrderByCreatedAtDesc(wallet);
    }

    public Wallet getWallet(Long walletId) {
    return walletRepository.findById(walletId)
            .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
}


}
