package com.example.banking_app.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.banking_app.dto.AccountInfo;
import com.example.banking_app.dto.BankResponse;
import com.example.banking_app.dto.EmailDetails;
import com.example.banking_app.dto.UserRequest;
import com.example.banking_app.entity.User;
import com.example.banking_app.repository.UserRepo;
import com.example.banking_app.service.EmailService;
import com.example.banking_app.service.UserService;
import com.example.banking_app.utils.AccountUtils;


@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepo userRepo;
	
	@Autowired
	EmailService emailService;
	
	public UserServiceImpl(UserRepo userRepo) {
		this.userRepo = userRepo;
	}


	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		
		if(userRepo.existsByEmail(userRequest.getEmail())) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
					.accountInfo(null)
					.build();
			
		}
		
		User newUser = User.builder()
				.firstName(userRequest.getFirstName())
				.lastName(userRequest.getLastName())
				.gender(userRequest.getGender())
				.address(userRequest.getAddress())
				.stateOfOrigin(userRequest.getStateOfOrigin())
				.accountNumber(AccountUtils.generateAccountNumber())
				.accountBalance(BigDecimal.ZERO)
				.email(userRequest.getEmail())
				.phoneNumber(userRequest.getPhoneNumber())
				.status("ACTIVE")
				.build();
		
		
		User savedUser = userRepo.save(newUser);
//		send email alert to the user
		
		EmailDetails emailDetails = EmailDetails.builder()
				.recipient(savedUser.getEmail())
				.subject("Account created successfully.")
				.messageBody("Congrats " + savedUser.getFirstName()+ " " + savedUser.getLastName() + " your account has been created successfully\n"
						+ "Your Account no. is : " + savedUser.getAccountNumber())
				.build();
		
		emailService.sendEmailAlert(emailDetails);
		
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREATED_CODE)
				.responseMessage(AccountUtils.ACCOUNT_CREATED_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountbalance(savedUser.getAccountBalance())
						.accountNumber(savedUser.getAccountNumber())
						.accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
						.build())
				.build();
	}

}
