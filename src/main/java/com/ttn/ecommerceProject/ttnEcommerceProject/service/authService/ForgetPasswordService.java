package com.ttn.ecommerceProject.ttnEcommerceProject.service.authService;


import com.ttn.ecommerceProject.ttnEcommerceProject.dto.forgetPasswordDto.ForgetPasswordRequest;
import com.ttn.ecommerceProject.ttnEcommerceProject.dto.resetPasswordDto.ResetPasswordRequest;
import com.ttn.ecommerceProject.ttnEcommerceProject.entity.PasswordResetToken;
import com.ttn.ecommerceProject.ttnEcommerceProject.entity.User;
import com.ttn.ecommerceProject.ttnEcommerceProject.repo.PasswordResetTokenRepo;
import com.ttn.ecommerceProject.ttnEcommerceProject.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForgetPasswordService {
    private final UserRepo userRepo;
    private final PasswordResetTokenRepo passwordResetTokenRepo ;
    private final PasswordEncoder  passwordEncoder;

    public void forgetPassword(ForgetPasswordRequest rqst){
        User user = userRepo.findByEmail(rqst.getEmail())
                .orElseThrow(() -> new RuntimeException("user not found"));

        passwordResetTokenRepo.findByUser(user)
                .ifPresent(passwordResetTokenRepo::delete);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDateTime(LocalDateTime.now().plusMinutes(15));


        passwordResetTokenRepo.save(resetToken);

        System.out.println("Reset token: " + token);

    }

    public void resetPassword(ResetPasswordRequest rqst){
        PasswordResetToken resetToken = passwordResetTokenRepo.findByToken(rqst.getToken())
                .orElseThrow(()->new RuntimeException("Invalid token"));

        if(resetToken.getExpiryDateTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(rqst.getNewPassword()));
        userRepo.save(user);

        passwordResetTokenRepo.delete(resetToken);
    }


}

