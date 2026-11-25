package com.example.emailtemplategenerator.service;

import com.example.emailtemplategenerator.client.GeminiClient;
import com.example.emailtemplategenerator.dto.GenerateEmailRequest;
import com.example.emailtemplategenerator.dto.GenerateEmailResponse;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final GeminiClient geminiClient;

    public EmailService(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    public GenerateEmailResponse generateEmail(GenerateEmailRequest request) {
        String prompt = "Write a short email. Purpose: " + request.getPurpose()
                + ". Recipient name: " + request.getRecipientName()
                + ". Tone: " + request.getTone() + ".";

        String text = geminiClient.generate(prompt);
        return new GenerateEmailResponse("Generated email", text);
    }
}