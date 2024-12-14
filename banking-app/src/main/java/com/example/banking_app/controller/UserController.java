package com.example.banking_app.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.banking_app.dto.BankResponse;
import com.example.banking_app.dto.CreditAndDebitRequest;
import com.example.banking_app.dto.EnquiryRequest;
import com.example.banking_app.dto.TransferRequest;
import com.example.banking_app.dto.UserRequest;
import com.example.banking_app.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/")
	public BankResponse createAccount(@RequestBody UserRequest userRequest) {
		return userService.createAccount(userRequest);
	}
	
	
	@PostMapping("/balanceEnquiry")
	public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request) {
		return userService.balanceEnquiry(request);
	}
	
	@PostMapping("/nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest request) {
		return userService.nameEnquiry(request);
	}
	
	@PostMapping("/credit")
	public BankResponse creditAccount(@RequestBody CreditAndDebitRequest request) {
		return userService.creditAccount(request);
	}
	
	@PostMapping("/debit")
	public BankResponse debitAccount (@RequestBody CreditAndDebitRequest request) {
		return userService.debitAccount(request);
	}
	
	
	@PostMapping("/transfer")
	public BankResponse transfer(@RequestBody TransferRequest request) {
		return userService.transferMoney(request);
	}
}
