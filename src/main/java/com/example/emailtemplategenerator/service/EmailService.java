package com.example.emailtemplategenerator.service;

import com.example.emailtemplategenerator.dto.GenerateEmailRequest;
import com.example.emailtemplategenerator.dto.GenerateEmailResponse;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public GenerateEmailResponse generateEmail(GenerateEmailRequest request) {
        return new GenerateEmailResponse(
                "Placeholder subject",
                "Hi " + request.getRecipientName() + ", this is a placeholder email."
        );
    }
}