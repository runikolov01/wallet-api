package com.example.wallet_api.model;

public class Wallet {

    private Long id;
    private Long userId;
    private Double balance;

    public Wallet() {
        this.balance = 0.0;
    }

    public Wallet(Long id, Long userId, Double balance) {
    }

    public Wallet(Long userId, Double balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}

