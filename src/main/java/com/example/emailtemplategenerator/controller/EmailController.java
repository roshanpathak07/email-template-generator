package com.example.emailtemplategenerator.controller;

import com.example.emailtemplategenerator.dto.GenerateEmailRequest;
import com.example.emailtemplategenerator.dto.GenerateEmailResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {

    @PostMapping("/generate")
    public GenerateEmailResponse generate(@Valid @RequestBody GenerateEmailRequest request) {
        return new GenerateEmailResponse(
                "Placeholder subject",
                "Hi " + request.getRecipientName() + ", this is a placeholder email."
        );
    }
}
