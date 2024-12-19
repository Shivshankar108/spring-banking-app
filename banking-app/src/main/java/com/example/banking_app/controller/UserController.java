package com.example.banking_app.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.banking_app.dto.BankResponse;
import com.example.banking_app.dto.CreditAndDebitRequest;
import com.example.banking_app.dto.EnquiryRequest;
import com.example.banking_app.dto.LoginDto;
import com.example.banking_app.dto.TransferRequest;
import com.example.banking_app.dto.UserRequest;
import com.example.banking_app.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
public class UserController {

	UserService userService;


//  Constructor Injection dependency
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	
	@Operation(
			summary = "Create New User Account",
			description = "Creating a new user and assigning an account number"
	)
	@ApiResponse(
			responseCode = "201",
			description = "Http Status 201 CREATED"
	)
	@PostMapping("/")
	public BankResponse createAccount(@RequestBody UserRequest userRequest) {
		return userService.createAccount(userRequest);
	}
	
	@PostMapping("/login")
	public BankResponse login(@RequestBody LoginDto loginDto) {
		return userService.login(loginDto);
	}
	
	@Operation(
			summary = "Balance Enquiry",
			description = "Provide with balance info of the given account number"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status 200 SUCCESS"
	)
	@PostMapping("/balanceEnquiry")
	public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request) {
		return userService.balanceEnquiry(request);
	}
	
	
	@Operation(
			summary = "Name Enquiry",
			description = "Provide with Account info of the given account number"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status 200 SUCCESS"
	)
	@PostMapping("/nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest request) {
		return userService.nameEnquiry(request);
	}
	
	
	@Operation(
			summary = "Amount Credit",
			description = "credit the given amount in the given account number if exists"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status 200 SUCCESS"
	)
	@PostMapping("/credit")
	public BankResponse creditAccount(@RequestBody CreditAndDebitRequest request) {
		return userService.creditAccount(request);
	}
	
	
	@Operation(
			summary = "Account Debite",
			description = "Debit amount from the given account number if the account exists and the amount is sufficient in account"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status 200 SUCCESS"
	)
	@PostMapping("/debit")
	public BankResponse debitAccount (@RequestBody CreditAndDebitRequest request) {
		return userService.debitAccount(request);
	}

	
	@Operation(
			summary = "Transfer of given amount from one account to another",
			description = "Transfer of given amount from one acount to another if the both given account exists and the sender has a sufficient balance in their account"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status 200 SUCCESS"
	)
	@PostMapping("/transfer")
	public BankResponse transfer(@RequestBody TransferRequest request) {
		return userService.transferMoney(request);
	}
}
