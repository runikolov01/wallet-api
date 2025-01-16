package com.example.wallet_api.service.impl;

import com.example.wallet_api.model.Wallet;
import com.example.wallet_api.repository.WalletRepository;
import com.example.wallet_api.service.WalletService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            loggedIn = false;
            return "redirect:/login";
        }
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("userId", userId);
        model.addAttribute("currentPage", "createWallet");

        return "createWallet";
    }

    @Override
    public void createWallet(HttpSession session, String walletName, String currency) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            Wallet wallet = new Wallet();
            wallet.setUserId(userId);
            wallet.setName(walletName);
            wallet.setCurrency(currency);
            wallet.setBalance(0.0);
            walletRepository.save(wallet);
        }
    }

    @Override
    public String openWalletDetails(Long walletId, HttpSession session, Model model) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Wallet wallet = getWalletById(walletId);
        model.addAttribute("wallet", wallet);
        model.addAttribute("currentPage", "walletDetails");


        return "walletDetails";
    }

    @Override
    public Wallet getWalletById(Long walletId) {
        Optional<Wallet> wallet = walletRepository.findById(walletId);
        return wallet.orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    @Override
    public String handleDeposit(Long walletId, double amount, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        depositMoney(walletId, amount);
        session.setAttribute("walletId", walletId);

        return "redirect:/walletDetails/" + walletId;
    }

    @Override
    public void depositMoney(Long walletId, double amount) {
        Wallet wallet = getWalletById(walletId);
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);
    }

    @Override
    public ResponseEntity<String> withdrawMoney(Long userId, Long walletId, double amount) {
        Wallet wallet = getWalletById(walletId);
        if (!wallet.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access to wallet");
        }

        if (wallet.getBalance() >= amount) {
            wallet.setBalance(wallet.getBalance() - amount);
            walletRepository.save(wallet);
            return ResponseEntity.ok("Withdrawal successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient balance");
        }
    }
}