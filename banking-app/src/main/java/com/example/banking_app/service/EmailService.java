package com.example.banking_app.service;

import com.example.banking_app.dto.EmailDetails;

public interface EmailService {

	void sendEmailAlert(EmailDetails emailDetails);
}
