package com.example.wallet_api.service;

import com.example.wallet_api.model.Wallet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public interface WalletService {

    List<Wallet> getWalletsByUserId(Long userId);

    String openCreateWalletForm(Model model, HttpSession session);

    void createWallet(Long userId, String walletName, String currency);

    Wallet getWalletById(Long walletId);

    void depositMoney(Long walletId, double amount);

    void withdrawMoney(Long walletId, double amount);
}