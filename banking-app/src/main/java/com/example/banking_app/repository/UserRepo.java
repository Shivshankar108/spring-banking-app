package com.example.banking_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.banking_app.entity.User;

public interface UserRepo extends JpaRepository<User, Long>{

	Boolean existsByEmail(String email);
	
	Optional<User> findByEmail(String email);
	
	Boolean existsByAccountNumber(String accountNum);
	
	User findByAccountNumber(String accountNumber);
	
	
}

