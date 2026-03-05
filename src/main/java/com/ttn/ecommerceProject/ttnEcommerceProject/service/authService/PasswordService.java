package com.ttn.ecommerceProject.ttnEcommerceProject.service.authService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    private final PasswordEncoder passwordEncoder;

    public PasswordService(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public void passwordMatch(String p1 , String p2){
        if(p1==null || p2 == null || !p1.equals(p2)){
            throw new RuntimeException("Password and confirm password must match");

        }
    }

    public String encode(String password){
        return passwordEncoder.encode(password);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
