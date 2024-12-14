package com.example.banking_app.service;

import com.example.banking_app.dto.BankResponse;
import com.example.banking_app.dto.CreditAndDebitRequest;
import com.example.banking_app.dto.EnquiryRequest;
import com.example.banking_app.dto.UserRequest;

public interface UserService {

	BankResponse createAccount(UserRequest userRequest);
	
	BankResponse balanceEnquiry(EnquiryRequest request);
	
	String nameEnquiry(EnquiryRequest request);

	BankResponse creditAccount(CreditAndDebitRequest request);
	
	BankResponse debitAccount(CreditAndDebitRequest request);
}
