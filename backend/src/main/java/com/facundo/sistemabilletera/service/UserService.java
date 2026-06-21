package com.facundo.sistemabilletera.service;
import com.facundo.sistemabilletera.model.AppUser;
import com.facundo.sistemabilletera.model.Wallet;
import com.facundo.sistemabilletera.repository.AppUserRepository;
import com.facundo.sistemabilletera.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class UserService {


    private final AppUserRepository appUserRepository;
    private final WalletRepository walletRepository;

    public UserService(AppUserRepository appUserRepository, WalletRepository walletRepository) {
        this.appUserRepository = appUserRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public AppUser createUser(String fullName, String email, String password) {
        if (appUserRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        AppUser user = new AppUser();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);

        AppUser savedUser = appUserRepository.save(user);

        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        walletRepository.save(wallet);

        return savedUser;
    }
}
