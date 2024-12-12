package com.example.banking_app.service;

import com.example.banking_app.dto.BankResponse;
import com.example.banking_app.dto.UserRequest;

public interface UserService {

	BankResponse createAccount(UserRequest userRequest);
}
