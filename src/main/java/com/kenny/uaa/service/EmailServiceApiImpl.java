package com.kenny.uaa.service;

import com.sendgrid.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@ConditionalOnProperty(prefix = "kenny.email-provider", name = "name", havingValue = "api")
@RequiredArgsConstructor
@Service
public class EmailServiceApiImpl implements EmailService{

    private final SendGrid sendGrid;

    @Override
    public void send(String email, String msg) {
        val from = new Email("service@kenny.com");
        val subject = "Kenny Spring Security Login Verification Code";
        val to = new Email(email);
        val content = new Content("text/plain", "Your verification code is: " + msg);
        val mail = new Mail(from, subject, to, content);
        val request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            if (response.getStatusCode() == 202) {
                log.info("Email sent successfully");
            } else {
                log.error(response.getBody());
            }
        } catch (IOException e) {
            log.error("Exception occurred while sending email: {}", e.getLocalizedMessage());
        }

    }
}
