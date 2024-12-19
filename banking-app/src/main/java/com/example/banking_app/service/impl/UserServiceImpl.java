package com.example.banking_app.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.banking_app.config.JwtTokenProvider;
import com.example.banking_app.dto.AccountInfo;
import com.example.banking_app.dto.BankResponse;
import com.example.banking_app.dto.CreditAndDebitRequest;
import com.example.banking_app.dto.EmailDetails;
import com.example.banking_app.dto.EnquiryRequest;
import com.example.banking_app.dto.LoginDto;
import com.example.banking_app.dto.TransactionDto;
import com.example.banking_app.dto.TransferRequest;
import com.example.banking_app.dto.UserRequest;
import com.example.banking_app.entity.Role;
import com.example.banking_app.entity.User;
import com.example.banking_app.repository.UserRepo;
import com.example.banking_app.service.EmailService;
import com.example.banking_app.service.TransactionService;
import com.example.banking_app.service.UserService;
import com.example.banking_app.utils.AccountUtils;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	TransactionService transactionService;

	@Autowired
	EmailService emailService;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;

	
//	public UserServiceImpl() {
//	}
//	
//	
//	@Autowired
//	public UserServiceImpl(UserRepo userRepo, TransactionService transactionService, EmailService emailService,PasswordEncoder passwordEncoder) {
//		super();
//		this.userRepo = userRepo;
//		this.transactionService = transactionService;
//		this.emailService = emailService;
//		this.passwordEncoder = passwordEncoder; 
//	}


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
				.password(passwordEncoder.encode(userRequest.getPassword()))
				.phoneNumber(userRequest.getPhoneNumber())
				.status("ACTIVE")
				.role(Role.valueOf("ROLE_ADMIN"))
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

	@Override
	public BankResponse login(LoginDto loginDto) {
		
		Authentication authentication = null;
		authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
		);
		
		EmailDetails loginAlert = EmailDetails.builder()
				.subject("You're logged in!")
				.recipient(loginDto.getEmail())
				.messageBody("You logged into your account. If you did not initiate this request, please contact your bank.")
				.build();
		
		emailService.sendEmailAlert(loginAlert);
		
		return BankResponse.builder()
				.responseCode("Login Success")
				.responseMessage(jwtTokenProvider.generateToken(authentication))
				.build();
	}
	
	
	@Override
	public BankResponse balanceEnquiry(EnquiryRequest request) {
//		check if the provided account num exists in db
		
		boolean isAccountExist = userRepo.existsByAccountNumber(request.getAccountNumber());
		
		if(!isAccountExist) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		User foundUser = userRepo.findByAccountNumber(request.getAccountNumber());
		
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
				.responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountNumber(foundUser.getAccountNumber())
						.accountName(foundUser.getFirstName()+ " " + foundUser.getLastName())
						.accountbalance(foundUser.getAccountBalance())
						.build())
				.build();
	}


	@Override
	public String nameEnquiry(EnquiryRequest request) {
		
		boolean isAccountExist = userRepo.existsByAccountNumber(request.getAccountNumber());
		
			if(!isAccountExist) {
				return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
			}
			
			User foundUser = userRepo.findByAccountNumber(request.getAccountNumber());
			
			return foundUser.getFirstName() + " " + foundUser.getLastName();
	}


	@Override
	public BankResponse creditAccount(CreditAndDebitRequest request) {
//		Checking if Account exists
		boolean isAccountExist = userRepo.existsByAccountNumber(request.getAccountNumber());
				
		if(!isAccountExist) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		User userToCredit = userRepo.findByAccountNumber(request.getAccountNumber());
		userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
		
		userRepo.save(userToCredit);
		
//		Save Transaction
		TransactionDto transactionDto = TransactionDto.builder()
				.accountNumber(userToCredit.getAccountNumber())
				.transactionType("CREDIT")
				.amount(request.getAmount())
				.status("")
				.build();
		
		transactionService.saveTransaction(transactionDto);
		
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
				.responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
						.accountNumber(userToCredit.getAccountNumber())
						.accountbalance(userToCredit.getAccountBalance())
						.build())
				.build();
	}


	@Override
	public BankResponse debitAccount(CreditAndDebitRequest request) {
		boolean isAccountExist = userRepo.existsByAccountNumber(request.getAccountNumber());
		
		if(!isAccountExist) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		User userToDebit = userRepo.findByAccountNumber(request.getAccountNumber());
		
		
		// Get the BigDecimal values for available balance and debit amount
	    BigDecimal availableAmount = userToDebit.getAccountBalance();
	    BigDecimal debitAmount = request.getAmount();

	    // Compare the available balance and debit amount using compareTo()
	    if (availableAmount.compareTo(debitAmount) < 0) {
			return BankResponse.builder()
					.responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
		userRepo.save(userToDebit);
		
//		Save Transaction
		TransactionDto transactionDto = TransactionDto.builder()
				.accountNumber(userToDebit.getAccountNumber())
				.transactionType("DEBIT")
				.amount(request.getAmount())
				.status("")
				.build();
		
		transactionService.saveTransaction(transactionDto);
		
		
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
				.responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
						.accountNumber(userToDebit.getAccountNumber())
						.accountbalance(userToDebit.getAccountBalance())
						.build())
				.build();
	}


	@Override
	public BankResponse transferMoney(TransferRequest request) {
		
		boolean isSourceAccountExist = userRepo.existsByAccountNumber(request.getSourceAccountNumber());
		boolean isDestinationAccountExist = userRepo.existsByAccountNumber(request.getDestinationAccountNumber());
		
		if(!isSourceAccountExist || !isDestinationAccountExist) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		User sourceAccountUser  = userRepo.findByAccountNumber(request.getSourceAccountNumber());
		User destinationAccountUser  = userRepo.findByAccountNumber(request.getDestinationAccountNumber());
		
		BigDecimal availableAmount = sourceAccountUser.getAccountBalance();
	    BigDecimal debitAmount = request.getAmount();

	    // Compare the available balance and debit amount using compareTo()
	    if (availableAmount.compareTo(debitAmount) < 0) {
			return BankResponse.builder()
					.responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
					.accountInfo(null)
					.build();
		}
	    
	    sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
	    userRepo.save(sourceAccountUser);
	    
	    EmailDetails debitAlert = EmailDetails.builder()
	    		.subject("Debit Alert")
	    		.recipient(sourceAccountUser.getEmail())
	    		.messageBody("Your account no. "+ sourceAccountUser.getAccountNumber() + " has been debited " + request.getAmount() + " rupees.")
	    		.build();
	    
	    emailService.sendEmailAlert(debitAlert);
	    
	    destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
	    userRepo.save(destinationAccountUser);
		
	    EmailDetails creditAlert = EmailDetails.builder()
	    		.subject("Credit Alert")
	    		.recipient(destinationAccountUser.getEmail())
	    		.messageBody("Your account no. "+ destinationAccountUser.getAccountNumber() + " has been credited " + request.getAmount() + " rupees.")
	    		.build();
	    
	    emailService.sendEmailAlert(creditAlert);
	    
//		Save Transaction
		TransactionDto transactionDto = TransactionDto.builder()
				.accountNumber(destinationAccountUser.getAccountNumber())
				.transactionType("DEBIT")
				.amount(request.getAmount())
				.status("")
				.build();
		
		transactionService.saveTransaction(transactionDto);
	    
		return BankResponse.builder()
				.responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
				.responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
				.accountInfo(null)
				.build();
	}

	
}
