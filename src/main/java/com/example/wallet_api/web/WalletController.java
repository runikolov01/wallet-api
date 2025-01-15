package com.example.wallet_api.web;

import com.example.wallet_api.model.User;
import com.example.wallet_api.model.Wallet;
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

import java.util.List;
import java.util.Optional;

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
        Long userId = (Long) session.getAttribute("userId");
        Optional<User> loggedUserOptional = userService.getUserById(userId);
        model.addAttribute("loggedUser", loggedUserOptional.orElse(null));
        if (userId == null) {
            return "redirect:/login";
        }
        List<Wallet> wallets = walletService.getWalletsByUserId(userId);
        model.addAttribute("wallets", wallets);
        return "myProfile";
    }


    @GetMapping("/createWallet")
    public String showCreateWalletForm(Model model, HttpSession session, HttpServletRequest request) {
        return walletService.openCreateWalletForm(model, session);
    }

    @PostMapping("/createWallet")
    public String createWallet(HttpSession session, String walletName, String currency) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            walletService.createWallet(userId, walletName, currency);
        }
        return "redirect:/myProfile";
    }

    @GetMapping("/walletDetails/{walletId}")
    public String walletDetails(@PathVariable Long walletId, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Wallet wallet = walletService.getWalletById(walletId);
        model.addAttribute("wallet", wallet);

        return "walletDetails";
    }

    @Transactional
    @PostMapping("/depositMoney")
    public String depositMoney(HttpSession session, @RequestParam Long walletId, @RequestParam double amount) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        walletService.depositMoney(walletId, amount);
        session.setAttribute("walletId", walletId);
        return "redirect:/walletDetails/" + walletId;
    }

    @PutMapping("/withdrawMoney")
    public ResponseEntity<String> withdrawMoney(HttpSession session, @RequestParam Long walletId, @RequestParam double amount) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }

        try {
            walletService.withdrawMoney(walletId, amount);
            session.setAttribute("walletId", walletId);
            return ResponseEntity.ok("Withdrawal successful");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Withdrawal failed: " + e.getMessage());
        }
    }
}