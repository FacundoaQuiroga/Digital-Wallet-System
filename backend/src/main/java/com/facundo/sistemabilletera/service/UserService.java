package com.facundo.sistemabilletera.service;
import com.facundo.sistemabilletera.model.AppUser;
import com.facundo.sistemabilletera.model.Wallet;
import com.facundo.sistemabilletera.repository.AppUserRepository;
import com.facundo.sistemabilletera.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service

public class UserService {


    private final AppUserRepository appUserRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(AppUserRepository appUserRepository, WalletRepository walletRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AppUser createUser(String fullName, String email, String password) {
        if (appUserRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        AppUser user = new AppUser();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        AppUser savedUser = appUserRepository.save(user);

        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        walletRepository.save(wallet);

        return savedUser;
    }

    public AppUser login(String email, String password) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }

    public AppUser getByEmail(String email) {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
