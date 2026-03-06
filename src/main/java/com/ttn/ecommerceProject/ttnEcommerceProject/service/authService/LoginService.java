package com.ttn.ecommerceProject.ttnEcommerceProject.service.authService;


import com.ttn.ecommerceProject.ttnEcommerceProject.dto.loginDto.LoginRequestDto;
import com.ttn.ecommerceProject.ttnEcommerceProject.dto.loginDto.LoginResponseDto;
import com.ttn.ecommerceProject.ttnEcommerceProject.entity.Role;
import com.ttn.ecommerceProject.ttnEcommerceProject.entity.User;
import com.ttn.ecommerceProject.ttnEcommerceProject.repo.UserRepo;
import com.ttn.ecommerceProject.ttnEcommerceProject.service.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepo userRepo;
    private final PasswordService passwordService;
    private final JwtService jwtService;

    @Transactional
    public LoginResponseDto login(LoginRequestDto req){
        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(()-> new RuntimeException("Invalid credentials"));

        if(!passwordService.matches(req.getPassword() , user.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }
        if(!user.isActive()){
            throw new RuntimeException("Account is not activated");
        }
        if(user.isDeleted()){
            throw new RuntimeException("Account is deleted");
        }




        Set<String> roles = user.getRoles().stream()
                .map(Role::getAuthority)
                .collect(Collectors.toSet());

        String token = jwtService.generateToken(user.getEmail() , 86400000L);

        return new LoginResponseDto(
                token,
                user.getEmail(),
                roles
        );

    }
}
