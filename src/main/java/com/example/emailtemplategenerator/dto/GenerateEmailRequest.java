package com.example.emailtemplategenerator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class GenerateEmailRequest {

    @NotBlank(message = "purpose is required")
    @Size(max = 300, message = "purpose must be at most 300 characters")
    private String purpose;

    @NotBlank(message = "recipient_name is required")
    @Size(max = 100, message = "recipient_name must be at most 100 characters")
    @JsonProperty("recipient_name")
    private String recipientName;

    @NotBlank(message = "tone is required")
    @Size(max = 50, message = "tone must be at most 50 characters")
    private String tone;



    public GenerateEmailRequest() {
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }
}