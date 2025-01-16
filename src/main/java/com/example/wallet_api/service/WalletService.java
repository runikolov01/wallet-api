package com.example.wallet_api.service;

import com.example.wallet_api.model.Wallet;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public interface WalletService {

    List<Wallet> getWalletsByUserId(Long userId);

    String openCreateWalletForm(Model model, HttpSession session);

    void createWallet(HttpSession session, String walletName, String currency);

    String openWalletDetails(Long walletId, HttpSession session, Model model);

    Wallet getWalletById(Long walletId);

    String handleDeposit(Long walletId, double amount, HttpSession session);

    void depositMoney(Long walletId, double amount);

    ResponseEntity<String> withdrawMoney(Long userId, Long walletId, double amount);
}