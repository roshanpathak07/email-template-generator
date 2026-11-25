package com.example.emailtemplategenerator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenerateEmailRequest {

    private String purpose;

    @JsonProperty("recipient_name")
    private String recipientName;

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