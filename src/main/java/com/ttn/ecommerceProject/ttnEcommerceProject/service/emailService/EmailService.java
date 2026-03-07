package com.ttn.ecommerceProject.ttnEcommerceProject.service.emailService;


import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    void sendActivationEmail(String toEmail, String text, String token);

    void sendResetPasswordEmail(String toEmail, String token);
}
