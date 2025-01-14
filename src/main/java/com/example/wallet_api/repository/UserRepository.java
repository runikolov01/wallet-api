package com.example.wallet_api.repository;

import com.example.wallet_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email); // Method to find user by email

    boolean existsByEmail(String email);

    boolean existsByPin(String pin);
}