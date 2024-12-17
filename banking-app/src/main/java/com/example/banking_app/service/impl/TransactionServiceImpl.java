package com.example.banking_app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.banking_app.dto.TransactionDto;
import com.example.banking_app.entity.Transaction;
import com.example.banking_app.repository.TransactionRepo;
import com.example.banking_app.service.TransactionService;

@Component
public class TransactionServiceImpl  implements TransactionService{

//	constructor injection dependency
	@Autowired
	TransactionRepo transRepo;

	public TransactionServiceImpl(TransactionRepo transRepo) {
		this.transRepo = transRepo;
	}

	
	@Override
	public void saveTransaction(TransactionDto transactionDto) {
		Transaction transaction = Transaction.builder()
				.transactionType(transactionDto.getTransactionType())
				.accountNumber(transactionDto.getAccountNumber())
				.amount(transactionDto.getAmount())
				.status("Success")
				.build();
		
		transRepo.save(transaction);
		System.out.println("Transaction saved successfully");
	}

}
