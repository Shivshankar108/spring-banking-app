package com.example.banking_app.service;

import com.example.banking_app.dto.TransactionDto;

public interface TransactionService {

	void saveTransaction(TransactionDto transactionDto);
}
