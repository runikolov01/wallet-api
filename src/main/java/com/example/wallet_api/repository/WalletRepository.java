package com.example.wallet_api.repository;

import com.example.wallet_api.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    List<Wallet> findByUserId(Long userId);
}