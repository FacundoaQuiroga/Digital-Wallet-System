package com.facundo.sistemabilletera.controller;

import com.facundo.sistemabilletera.model.Wallet;
import com.facundo.sistemabilletera.service.WalletService;
import org.springframework.web.bind.annotation.*;
import com.facundo.sistemabilletera.model.WalletTransaction;
import java.util.List;
import com.facundo.sistemabilletera.dto.WalletResponse;
import com.facundo.sistemabilletera.dto.TransactionResponse;
import java.math.BigDecimal;


@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/{walletId}/deposit")
    public WalletResponse deposit(
            @PathVariable Long walletId,
            @RequestBody DepositRequest request
    ) {
        Wallet wallet = walletService.deposit(walletId, request.amount());

        return new WalletResponse(
                wallet.getId(),
                wallet.getBalance(),
                wallet.getUser().getId(),
                wallet.getUser().getEmail()
        );
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
    public List<TransactionResponse> getTransactions(@PathVariable Long walletId) {
        return walletService.getTransactions(walletId)
                .stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getId(),
                        transaction.getWallet().getId(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getDescription(),
                        transaction.getCreatedAt()
                ))
                .toList();
    }

    public record DepositRequest(BigDecimal amount) {
    }
}
