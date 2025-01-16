package com.example.wallet_api.web;

import com.example.wallet_api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        return userService.openHomePage(model, session);
    }

    @GetMapping("/register")
    public String registerForm(Model model, HttpSession session, HttpServletRequest request) {
        return userService.openRegisterForm(model, session, request);
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String error, Model model, HttpSession session) {
        return userService.openLoginForm(error, model, session);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }


    @PostMapping("/register")
    public String registerSubmit(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String pin, @RequestParam String city, @RequestParam int telephoneNumber, @RequestParam String email, @RequestParam String password, @RequestParam String confirmPassword, RedirectAttributes redirectAttributes, Model model) {
        return userService.registerProcess(firstName, lastName, pin, city, telephoneNumber, email, password, confirmPassword, redirectAttributes, model);
    }


    @PostMapping("/login")
    public String loginSubmit(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        return userService.loginProcess(email, password, model, session);
    }
}