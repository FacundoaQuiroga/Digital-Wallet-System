package com.facundo.sistemabilletera.controller;

import com.facundo.sistemabilletera.dto.TransactionResponse;
import com.facundo.sistemabilletera.dto.WalletResponse;
import com.facundo.sistemabilletera.model.AppUser;
import com.facundo.sistemabilletera.model.Wallet;
import com.facundo.sistemabilletera.service.UserService;
import com.facundo.sistemabilletera.service.WalletService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

import java.util.List;

@RestController
@RequestMapping("/api/me")
public class MeController {


    private final UserService userService;
    private final WalletService walletService;

    public MeController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }

    @GetMapping("/wallet")
    public WalletResponse getMyWallet(Authentication authentication) {
        AppUser user = userService.getByEmail(authentication.getName());
        Wallet wallet = walletService.getWalletByUser(user);

        return new WalletResponse(
                wallet.getId(),
                wallet.getBalance(),
                wallet.getUser().getId(),
                wallet.getUser().getEmail()
        );
    }

    @GetMapping("/transactions")
    public List<TransactionResponse> getMyTransactions(Authentication authentication) {
        AppUser user = userService.getByEmail(authentication.getName());
        Wallet wallet = walletService.getWalletByUser(user);

        return walletService.getTransactions(wallet.getId())
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

    @PostMapping("/deposit")
    public WalletResponse depositToMyWallet(
            Authentication authentication,
            @Valid @RequestBody DepositRequest request
    ) {
        AppUser user = userService.getByEmail(authentication.getName());
        Wallet wallet = walletService.getWalletByUser(user);

        Wallet updatedWallet = walletService.deposit(wallet.getId(), request.amount());

        return new WalletResponse(
                updatedWallet.getId(),
                updatedWallet.getBalance(),
                updatedWallet.getUser().getId(),
                updatedWallet.getUser().getEmail()
        );
    }

    @PostMapping("/transfer")
    public void transferFromMyWallet(
            Authentication authentication,
            @Valid @RequestBody TransferRequest request
    ) {
        AppUser user = userService.getByEmail(authentication.getName());
        Wallet wallet = walletService.getWalletByUser(user);

        walletService.transfer(
                wallet.getId(),
                request.receiverWalletId(),
                request.amount()
        );
    }

    public record DepositRequest(
            @NotNull @Positive BigDecimal amount
    ) {
    }

    public record TransferRequest(
            @NotNull Long receiverWalletId,
            @NotNull @Positive BigDecimal amount
    ) {
    }


}
