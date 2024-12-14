package com.example.banking_app.utils;

import java.time.Year;

public class AccountUtils {
	
	public static final String ACCOUNT_EXISTS_CODE ="001";
	public static final String ACCOUNT_EXISTS_MESSAGE ="This User Already has an Account!";
	public static final String ACCOUNT_CREATED_CODE ="002";
	public static final String ACCOUNT_CREATED_MESSAGE ="Account has been successfully created!";
	public static final String ACCOUNT_NOT_EXIST_CODE = "003";
	public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User with the provided Account Number does not exist";
	public static final String ACCOUNT_FOUND_CODE="004";
	public static final String ACCOUNT_FOUND_MESSAGE="User Account Found";
	public static final String ACCOUNT_CREDITED_SUCCESS_CODE = "005";
	public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User account credited";
	public static final String INSUFFICIENT_BALANCE_CODE = "006";
	public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance";
	public static final String ACCOUNT_DEBITED_SUCCESS_CODE = "007";
	public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "Account has been successfully debited";
	
	

	
	public static String generateAccountNumber() {
	
		Year currentYear = Year.now();
		
		int min = 100000;
		int max = 999999;
		
		int randomNum = (int)Math.floor(Math.random() * (max - min + 1) + min);
		
		String Year = String.valueOf(currentYear);
		String randomNumber = String.valueOf(randomNum);
		StringBuilder accountNumber = new StringBuilder();
		
		return accountNumber.append(Year).append(randomNumber).toString();
	}
	
}
