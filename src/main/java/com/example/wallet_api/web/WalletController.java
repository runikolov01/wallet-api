package com.example.wallet_api.web;

import com.example.wallet_api.model.User;
import com.example.wallet_api.model.Wallet;
import com.example.wallet_api.service.UserService;
import com.example.wallet_api.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

}