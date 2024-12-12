package com.example.banking_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.banking_app.entity.User;

public interface UserRepo extends JpaRepository<User, Long>{

	Boolean existsByEmail(String email);
}
