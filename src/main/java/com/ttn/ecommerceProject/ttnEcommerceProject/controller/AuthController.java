package com.ttn.ecommerceProject.ttnEcommerceProject.controller;


import com.ttn.ecommerceProject.ttnEcommerceProject.dto.loginDto.LoginRequestDto;
import com.ttn.ecommerceProject.ttnEcommerceProject.dto.loginDto.LoginResponseDto;
import com.ttn.ecommerceProject.ttnEcommerceProject.service.authService.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final LoginService loginService;

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto req){
        return loginService.login(req);
    }

    @GetMapping("/test")
    public String test(){
        return "Test accepted!";
    }


}
