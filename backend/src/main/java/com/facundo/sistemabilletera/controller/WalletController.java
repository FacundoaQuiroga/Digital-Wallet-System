package com.facundo.sistemabilletera.controller;

import com.facundo.sistemabilletera.model.Wallet;
import com.facundo.sistemabilletera.service.WalletService;
import org.springframework.web.bind.annotation.*;
import com.facundo.sistemabilletera.model.WalletTransaction;
import java.util.List;
import com.facundo.sistemabilletera.dto.WalletResponse;
import com.facundo.sistemabilletera.controller.WalletController.DepositRequest;
import com.facundo.sistemabilletera.dto.TransactionResponse;
import java.math.BigDecimal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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
            @Valid @RequestBody DepositRequest request
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
    public void transfer(@Valid @RequestBody TransferRequest request) {
        walletService.transfer(
                request.senderWalletId(),
                request.receiverWalletId(),
                request.amount()
        );
    }

    public record TransferRequest(
            @NotNull Long senderWalletId,
            @NotNull Long receiverWalletId,
            @NotNull @Positive BigDecimal amount
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

    public record DepositRequest(@NotNull @Positive BigDecimal amount) {
    }
}
