package com.example.wallet_api.service;

import com.example.wallet_api.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Service
public interface UserService {
    void saveUser(User user);

    User findUserByEmail(String email);

    boolean checkIfUserExists(String email);

    String openHomePage(Model model, HttpSession session);

    String openRegisterForm(Model model, HttpSession session, HttpServletRequest request);

    String openLoginForm(@RequestParam(required = false) String error, Model model, HttpSession session);

    String openMyProfile(Model model, HttpSession session);

    boolean registerUser(User user);

    String registerProcess(String firstName, String lastName, String pin, String city, int telephoneNumber, String email, String password, String confirmPassword, RedirectAttributes redirectAttributes, Model model);

    String loginProcess(String email, String password, Model model, HttpSession session);

    Optional<User> getUserById(Long userId);

    User findByEmail(String email);

    boolean checkIfUserExistsByPin(String pin);

    boolean registerUser(User user, String confirmPassword);

    boolean verifyPassword(User user, String password);

    User getUserByEmail(String email);
}