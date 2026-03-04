package com.ttn.ecommerceProject.ttnEcommerceProject.service.emailService;


import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Async
    public void sendActivationMail(String email , String link){
        System.out.println("Activation mail sent to: " + email);
        System.out.println("Activation link: " + link);
    }
}
