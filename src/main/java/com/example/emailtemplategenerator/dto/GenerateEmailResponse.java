package com.example.emailtemplategenerator.dto;

public class GenerateEmailResponse {

    private String subject;
    private String body;

    public GenerateEmailResponse(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}