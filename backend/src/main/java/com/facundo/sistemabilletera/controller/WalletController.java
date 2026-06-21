package com.facundo.sistemabilletera.controller;

import com.facundo.sistemabilletera.model.Wallet;
import com.facundo.sistemabilletera.service.WalletService;
import org.springframework.web.bind.annotation.*;
import com.facundo.sistemabilletera.model.WalletTransaction;
import java.util.List;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/{walletId}/deposit")
    public Wallet deposit(
            @PathVariable Long walletId,
            @RequestBody DepositRequest request
    ) {
        return walletService.deposit(walletId, request.amount());
    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferRequest request) {
        walletService.transfer(
                request.senderWalletId(),
                request.receiverWalletId(),
                request.amount()
        );
    }

    public record TransferRequest(
            Long senderWalletId,
            Long receiverWalletId,
            BigDecimal amount
    ) {
    }

    @GetMapping("/{walletId}/transactions")
    public List<WalletTransaction> getTransactions(@PathVariable Long walletId) {
        return walletService.getTransactions(walletId);
    }

    public record DepositRequest(BigDecimal amount) {
    }
}
