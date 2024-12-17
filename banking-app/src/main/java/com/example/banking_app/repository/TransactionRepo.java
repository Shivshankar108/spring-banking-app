package com.example.banking_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.banking_app.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, String>{

}
