package com.example.emailtemplategenerator.service;

import com.example.emailtemplategenerator.client.GeminiClient;
import com.example.emailtemplategenerator.dto.GenerateEmailRequest;
import com.example.emailtemplategenerator.dto.GenerateEmailResponse;
import com.example.emailtemplategenerator.exception.AiServiceException;
import com.example.emailtemplategenerator.prompt.EmailPromptBuilder;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final String SUBJECT_MARKER = "SUBJECT:";
    private static final String BODY_MARKER = "BODY:";

    private final GeminiClient geminiClient;
    private final EmailPromptBuilder promptBuilder;

    public EmailService(GeminiClient geminiClient, EmailPromptBuilder promptBuilder) {
        this.geminiClient = geminiClient;
        this.promptBuilder = promptBuilder;
    }

    public GenerateEmailResponse generateEmail(GenerateEmailRequest request) {
        String prompt = promptBuilder.build(request);
        String text = geminiClient.generate(prompt);
        return parse(text);
    }

    private GenerateEmailResponse parse(String text) {
        String trimmed = text.trim();
        int bodyIndex = trimmed.indexOf(BODY_MARKER);

        if (!trimmed.startsWith(SUBJECT_MARKER) || bodyIndex < 0) {
            throw new AiServiceException("AI response was not in the expected format", null);
        }

        String subject = trimmed.substring(SUBJECT_MARKER.length(), bodyIndex).trim();
        String body = trimmed.substring(bodyIndex + BODY_MARKER.length()).trim();
        return new GenerateEmailResponse(subject, body);
    }
}