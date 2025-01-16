package com.example.wallet_api.service.impl;

import com.example.wallet_api.model.User;
import com.example.wallet_api.model.Wallet;
import com.example.wallet_api.repository.UserRepository;
import com.example.wallet_api.service.UserService;
import com.example.wallet_api.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final WalletService walletService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, @Qualifier("userDetailsService") UserDetailsService userDetailsService, WalletService walletService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.walletService = walletService;
    }

    @Override
    @Transactional
    public void saveUser(User person) {
        userRepository.save(person);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean checkIfUserExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public String openHomePage(Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);
        model.addAttribute("currentPage", "home");

        Long userId = (Long) session.getAttribute("userId");

        if (loggedIn && userId != null) {
            Optional<User> user = getUserById(userId);
            if (user.isPresent()) {

                model.addAttribute("loggedUser", user.get());
                session.setAttribute("loggedUser", user.get());
            }
        }
        return "home";
    }

    @Override
    public String openRegisterForm(Model model, HttpSession session, HttpServletRequest request) {
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            model.addAllAttributes(inputFlashMap);
        }

        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);
        model.addAttribute("currentPage", "register");

        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            return "redirect:/myProfile";
        }

        return "register";
    }



    @Override
    public String openLoginForm(String error, Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        if (loggedIn == null) {
            loggedIn = false;
        }
        session.setAttribute("loggedIn", loggedIn);
        model.addAttribute("loggedIn", loggedIn);
        model.addAttribute("currentPage", "login");

        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            session.setAttribute("loggedIn", true);
            model.addAttribute("loggedIn", true);
            model.addAttribute("userId", userId);
            return "redirect:/myProfile";
        }

        if (error != null) {
            model.addAttribute("error", true);
        }

        return "login";
    }


    @Override
    public String openMyProfile(Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null || !Boolean.TRUE.equals(loggedIn)) {
            loggedIn = false;
            model.addAttribute("loggedIn", loggedIn);
            session.setAttribute("loggedIn", loggedIn);
            model.addAttribute("currentPage", "myProfile");
            return "redirect:/login";
        }

        Optional<User> loggedUserOptional = getUserById(userId);
        if (loggedUserOptional.isEmpty()) {
            return "redirect:/login";
        }

        User loggedUser = loggedUserOptional.get();
        model.addAttribute("loggedUser", loggedUser);
        session.setAttribute("loggedUser", loggedUser);
        model.addAttribute("loggedIn", true);

        List<Wallet> wallets = walletService.getWalletsByUserId(userId);
        model.addAttribute("wallets", wallets);

        return "myprofile";
    }


    @Override
    public boolean registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public String registerProcess(String firstName, String lastName, String pin, String city, int telephoneNumber, String email, String password, String confirmPassword, RedirectAttributes redirectAttributes, Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("pin", pin);
            model.addAttribute("city", city);
            model.addAttribute("telephoneNumber", telephoneNumber);
            model.addAttribute("email", email);
            model.addAttribute("error", "Passwords do not match.");

            return "register";

        }

        User emailUser = findByEmail(email);
        if (emailUser != null) {
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("pin", pin);
            model.addAttribute("city", city);
            model.addAttribute("telephoneNumber", telephoneNumber);
            model.addAttribute("email", email);
            model.addAttribute("error", "There is already a user registered with this email");

            return "register";

        }

        User user = new User();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPin(pin);
        user.setCity(city);
        user.setTelephoneNumber(telephoneNumber);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        try {
            saveUser(user);
            model.addAttribute("success", "The user is registered successfully!");
//            model.addAttribute("loggedUser", user);

            return "login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error while registering user");
            return "redirect:/register";
        }

    }


    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public String loginProcess(String email, String password, Model model, HttpSession session) {
        User user = getUserByEmail(email);


        if (user != null && verifyPassword(user, password)) {

            session.setAttribute("userId", user.getId());
            session.setAttribute("loggedUser", user);
            session.setAttribute("loggedIn", true);

            return "redirect:/myProfile";
        } else {
            model.addAttribute("email", email);

            model.addAttribute("error", "Invalid email or password");

            return "login";
        }
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean checkIfUserExistsByPin(String pin) {
        return userRepository.existsByPin(pin);
    }

    @Override
    @Transactional
    public boolean registerUser(User user, String confirmPassword) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return false;
        }

        if (checkIfUserExistsByPin(user.getPin())) {
            return false;
        }

        if (!user.getPassword().equals(confirmPassword)) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean verifyPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}