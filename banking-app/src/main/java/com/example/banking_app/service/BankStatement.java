package com.example.banking_app.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.banking_app.dto.EmailDetails;
import com.example.banking_app.entity.Transaction;
import com.example.banking_app.entity.User;
import com.example.banking_app.repository.TransactionRepo;
import com.example.banking_app.repository.UserRepo;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

	private TransactionRepo transactionRepo;
	
	private UserRepo userRepo;
	
	private EmailService emailService;
	
	private static final String FILE = "C:\\Users\\shank\\Documents\\MyStatement.pdf";
	
	
	
	public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException{
		
		User user = userRepo.findByAccountNumber(accountNumber);
		

//	     If the user is not found, log the error and exit the method
		if (user == null) {
	        log.error("User with account number {} not found.", accountNumber);
	        return Collections.emptyList();
	    }
		
		
		LocalDate start = LocalDate.parse(startDate,DateTimeFormatter.ISO_DATE);
		LocalDate end = LocalDate.parse(endDate,DateTimeFormatter.ISO_DATE);
		List<Transaction> transactionList = transactionRepo.findAll().stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
				.filter(transaction -> !transaction.getCreatedAt().isBefore(start) && !transaction.getCreatedAt().isAfter(end))
	            .toList();
		
		
//		calling the pdf generating function		
		designStatement(user,transactionList, start, end);
		
		EmailDetails emailDetails = EmailDetails.builder()
				.recipient(user.getEmail())
				.subject("STATEMENT OF ACCOUNT")
				.messageBody("Kindly find your requested acount statement attached!")
				.attachment(FILE)
				.build();
		
		emailService.sendEmailWithAttachment(emailDetails);
		
		return transactionList;
	}
	
	private void designStatement(User user, List<Transaction> transactions, LocalDate startDate, LocalDate endDate) throws FileNotFoundException, DocumentException {
		
		String customerName = user.getFirstName() + " " + user.getLastName();
		
		Rectangle statementSize = new Rectangle(PageSize.A4);
		Document document = new Document(statementSize);
		log.info("setting size of document");
		OutputStream outputStream = new FileOutputStream(FILE);
		PdfWriter.getInstance(document, outputStream);
		document.open();
		
		
//		first table for bank information
		
		PdfPTable bankInfoTable = new PdfPTable(1);
		PdfPCell bankName = new PdfPCell(new Phrase("Bank Name"));
		bankName.setBorder(0);
		bankName.setBackgroundColor(BaseColor.BLUE);
		bankName.setPadding(20f);
		PdfPCell bankAddress = new PdfPCell(new Phrase("Delhi, India"));
		
		
		bankInfoTable.addCell(bankName);
		bankInfoTable.addCell(bankAddress);
		
		
//		second table for statement information
		
		PdfPTable statementInfo = new PdfPTable(2);
		PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
		statement.setBorder(0);
		PdfPCell customerInfo = new PdfPCell(new Phrase("Customer Name : " + customerName));
		customerInfo.setBorder(0);
		PdfPCell start = new PdfPCell(new Phrase("Start Date : " + startDate));
		start.setBorder(0);
		PdfPCell end = new PdfPCell(new Phrase("Ens Date : " + endDate));
		end.setBorder(0);
		PdfPCell space = new PdfPCell();
		space.setBorder(0);
		PdfPCell address = new PdfPCell(new Phrase("Customer Address : " + user.getAddress()));
		address.setBorder(0);
		
		
//		adding all the statement cells on the statementinfo table
		
		statementInfo.addCell(start);
		statementInfo.addCell(statement);
		statementInfo.addCell(end);
		statementInfo.addCell(customerInfo);
		statementInfo.addCell(space);
		statementInfo.addCell(address);
		
//		third table for transactions
		
		PdfPTable transactionsTable = new PdfPTable(4);
		PdfPCell date = new PdfPCell(new Phrase("DATE"));
		date.setBackgroundColor(BaseColor.BLUE);
		date.setBorder(0);
		PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
		transactionType.setBackgroundColor(BaseColor.BLUE);
		transactionType.setBorder(0);
		PdfPCell amount = new PdfPCell(new Phrase("AMOUNT"));
		amount.setBackgroundColor(BaseColor.BLUE);
		amount.setBorder(0);
		PdfPCell status = new PdfPCell(new Phrase("STATUS"));
		status.setBackgroundColor(BaseColor.BLUE);
		status.setBorder(0);
		
		transactionsTable.addCell(date);
		transactionsTable.addCell(transactionType);
		transactionsTable.addCell(amount);
		transactionsTable.addCell(status);
		
		transactions.forEach(transaction -> {
			transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
			transactionsTable.addCell(new Phrase(transaction.getTransactionType()));
			transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
			transactionsTable.addCell(new Phrase(transaction.getStatus()));
		});
		
		
//		adding all tables on the pdf document
		
		document.add(bankInfoTable);
		document.add(statementInfo);
		document.add(transactionsTable);
		
		document.close();
		
	}
}
