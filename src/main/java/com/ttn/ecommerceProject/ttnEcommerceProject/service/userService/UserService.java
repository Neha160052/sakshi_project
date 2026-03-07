package com.ttn.ecommerceProject.ttnEcommerceProject.service.authService;

import com.ttn.ecommerceProject.ttnEcommerceProject.dto.forgetPasswordDto.ForgetPasswordRequest;
import com.ttn.ecommerceProject.ttnEcommerceProject.dto.loginDto.LoginRequestDto;
import com.ttn.ecommerceProject.ttnEcommerceProject.dto.loginDto.LoginResponseDto;
import com.ttn.ecommerceProject.ttnEcommerceProject.dto.resetPasswordDto.ResetPasswordRequest;
import com.ttn.ecommerceProject.ttnEcommerceProject.entity.*;
import com.ttn.ecommerceProject.ttnEcommerceProject.repo.PasswordResetTokenRepo;
import com.ttn.ecommerceProject.ttnEcommerceProject.repo.UserRepo;
import com.ttn.ecommerceProject.ttnEcommerceProject.service.emailService.EmailService;
import com.ttn.ecommerceProject.ttnEcommerceProject.service.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final ActivationService activationService;
    private final EmailService emailService;
    private final PasswordService passwordService;
    private final JwtService jwtService;
    private final PasswordResetTokenRepo passwordResetTokenRepo;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void activate(String tokenValue) {
        ActivationToken token = activationService.validateToken(tokenValue);
        User user = token.getUser();

        if (user.isActive()) {
            throw new RuntimeException("Account already activated");
        }

        user.setActive(true);
        userRepo.save(user);
        activationService.deleteToken(token);
    }

    @Transactional
    public void resendActivation(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isActive()) {
            throw new RuntimeException("Account already activated");
        }

        ActivationToken token = activationService.createToken(user);
        emailService.sendActivationEmail(
                user.getEmail(),
                "Account Activation",
                token.getToken()
        );
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto req) {
        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordService.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.isActive()) {
            throw new RuntimeException("Account is not activated");
        }

        if (user.isDeleted()) {
            throw new RuntimeException("Account is deleted");
        }

        Set<String> roles = user.getRoles().stream()
                .map(Role::getAuthority)
                .collect(Collectors.toSet());

        String token = jwtService.generateToken(user.getEmail(), 86400000L);

        return new LoginResponseDto(
                token,
                user.getEmail(),
                roles
        );
    }

    @Transactional
    public void forgetPassword(ForgetPasswordRequest rqst) {
        User user = userRepo.findByEmail(rqst.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

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

    @Transactional
    public void resetPassword(ResetPasswordRequest rqst) {
        PasswordResetToken resetToken = passwordResetTokenRepo.findByToken(rqst.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(rqst.getNewPassword()));
        userRepo.save(user);

        passwordResetTokenRepo.delete(resetToken);
    }
}