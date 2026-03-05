package com.ttn.ecommerceProject.ttnEcommerceProject.service.authService;

import com.ttn.ecommerceProject.ttnEcommerceProject.entity.ActivationToken;
import com.ttn.ecommerceProject.ttnEcommerceProject.entity.User;
import com.ttn.ecommerceProject.ttnEcommerceProject.repo.ActivationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ActivationService {
    private final ActivationTokenRepository tokenRepo;

    public ActivationService(ActivationTokenRepository tokenRepo){
        this.tokenRepo = tokenRepo;
    }

    public ActivationToken createToken(User user){

        tokenRepo.deleteByUserId(user.getId());

        ActivationToken token = new ActivationToken();

        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDateTime(LocalDateTime.now().plusHours(3));
        return tokenRepo.save(token);
    }

    public ActivationToken validateToken(String tokenValue){
        ActivationToken token = tokenRepo.findByToken(tokenValue)
                .orElseThrow(() -> new RuntimeException("Invalid activation token"));

        if(token.getExpiryDateTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token expired");
        }
        return token;
    }

    public void deleteToken(ActivationToken token){
        tokenRepo.delete(token);
    }

}
