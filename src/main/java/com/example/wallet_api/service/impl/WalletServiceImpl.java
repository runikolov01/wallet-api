package com.example.wallet_api.service.impl;

import com.example.wallet_api.model.Wallet;
import com.example.wallet_api.repository.WalletRepository;
import com.example.wallet_api.service.WalletService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public List<Wallet> getWalletsByUserId(Long userId) {
        return walletRepository.findByUserId(userId);
    }

    @Override
    public String openCreateWalletForm(Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null || !loggedIn) {
            return "redirect:/login";
        }

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("userId", userId);

        return "createWallet";
    }

    @Override
    public void createWallet(Long userId, String walletName, String currency) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setName(walletName);
        wallet.setCurrency(currency);
        wallet.setBalance(0.0);
        walletRepository.save(wallet);
    }

    @Override
    public Wallet getWalletById(Long walletId) {
        Optional<Wallet> wallet = walletRepository.findById(walletId);
        return wallet.orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    @Override
    public void depositMoney(Long walletId, double amount) {
        Wallet wallet = getWalletById(walletId);
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);
    }

    @Override
    public void withdrawMoney(Long walletId, double amount) {
        Wallet wallet = getWalletById(walletId);
        if (wallet.getBalance() >= amount) {
            wallet.setBalance(wallet.getBalance() - amount);
            walletRepository.save(wallet);
        } else {
            throw new RuntimeException("Insufficient balance");
        }
    }
}