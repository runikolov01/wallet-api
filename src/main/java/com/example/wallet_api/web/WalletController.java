package com.example.wallet_api.web;

import com.example.wallet_api.service.UserService;
import com.example.wallet_api.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WalletController {
    private final UserService userService;
    private final WalletService walletService;

    @Autowired
    public WalletController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }


    @GetMapping("/myProfile")
    public String getMyProfile(Model model, HttpSession session) {
        return userService.openMyProfile(model, session);
    }

    @GetMapping("/createWallet")
    public String showCreateWalletForm(Model model, HttpSession session, HttpServletRequest request) {
        return walletService.openCreateWalletForm(model, session);
    }

    @PostMapping("/createWallet")
    public String createWallet(HttpSession session, String walletName, String currency) {
        walletService.createWallet(session, walletName, currency);
        return "redirect:/myProfile";
    }

    @GetMapping("/walletDetails/{walletId}")
    public String walletDetails(@PathVariable Long walletId, HttpSession session, Model model) {
        return walletService.openWalletDetails(walletId, session, model);
    }

    @Transactional
    @PostMapping("/depositMoney")
    public String depositMoney(HttpSession session, @RequestParam Long walletId, @RequestParam double amount) {
        return walletService.handleDeposit(walletId, amount, session);
    }

    @PutMapping("/withdrawMoney")
    public ResponseEntity<String> withdrawMoney(HttpSession session, @RequestParam Long walletId, @RequestParam double amount) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }

        return walletService.withdrawMoney(userId, walletId, amount);
    }
}