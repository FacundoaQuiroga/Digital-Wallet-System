package com.facundo.sistemabilletera.service;

import com.facundo.sistemabilletera.model.Wallet;
import com.facundo.sistemabilletera.repository.WalletRepository;
import com.facundo.sistemabilletera.repository.WalletTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletTransactionRepository walletTransactionRepository;

    @InjectMocks
    private WalletService walletService;

    @Test
    void depositShouldIncreaseWalletBalanceAndSaveTransaction() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(new BigDecimal("100.00"));

        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Wallet result = walletService.deposit(1L, new BigDecimal("50.00"));

        assertEquals(new BigDecimal("150.00"), result.getBalance());

        verify(walletRepository).findById(1L);
        verify(walletRepository).save(wallet);
        verify(walletTransactionRepository).save(any());
    }

    @Test
    void transferShouldMoveMoneyBetweenWalletsAndSaveTransactions() {
        Wallet senderWallet = new Wallet();
        senderWallet.setId(1L);
        senderWallet.setBalance(new BigDecimal("500.00"));

        Wallet receiverWallet = new Wallet();
        receiverWallet.setId(2L);
        receiverWallet.setBalance(new BigDecimal("100.00"));

        when(walletRepository.findById(1L)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findById(2L)).thenReturn(Optional.of(receiverWallet));

        walletService.transfer(1L, 2L, new BigDecimal("150.00"));

        assertEquals(new BigDecimal("350.00"), senderWallet.getBalance());
        assertEquals(new BigDecimal("250.00"), receiverWallet.getBalance());

        verify(walletRepository).save(senderWallet);
        verify(walletRepository).save(receiverWallet);
        verify(walletTransactionRepository, times(2)).save(any());
    }

    @Test
    void transferShouldFailWhenSenderHasInsufficientFunds() {
        Wallet senderWallet = new Wallet();
        senderWallet.setId(1L);
        senderWallet.setBalance(new BigDecimal("50.00"));

        Wallet receiverWallet = new Wallet();
        receiverWallet.setId(2L);
        receiverWallet.setBalance(new BigDecimal("100.00"));

        when(walletRepository.findById(1L)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findById(2L)).thenReturn(Optional.of(receiverWallet));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> walletService.transfer(1L, 2L, new BigDecimal("150.00"))
        );

        assertEquals("Insufficient funds", exception.getMessage());

        assertEquals(new BigDecimal("50.00"), senderWallet.getBalance());
        assertEquals(new BigDecimal("100.00"), receiverWallet.getBalance());

        verify(walletRepository, never()).save(any());
        verify(walletTransactionRepository, never()).save(any());
    }
}
