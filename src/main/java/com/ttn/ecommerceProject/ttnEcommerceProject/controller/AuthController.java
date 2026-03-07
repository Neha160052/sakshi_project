package com.ttn.ecommerceProject.ttnEcommerceProject.controller;


import com.ttn.ecommerceProject.ttnEcommerceProject.dto.ApiResponse;
import com.ttn.ecommerceProject.ttnEcommerceProject.dto.forgetPasswordDto.ForgetPasswordRequest;
import com.ttn.ecommerceProject.ttnEcommerceProject.dto.loginDto.LoginRequestDto;
import com.ttn.ecommerceProject.ttnEcommerceProject.dto.loginDto.LoginResponseDto;
import com.ttn.ecommerceProject.ttnEcommerceProject.dto.registerDto.ResendActivationRequest;
import com.ttn.ecommerceProject.ttnEcommerceProject.dto.resetPasswordDto.ResetPasswordRequest;
import com.ttn.ecommerceProject.ttnEcommerceProject.service.authService.CustomerAuthService;
import com.ttn.ecommerceProject.ttnEcommerceProject.service.authService.ForgetPasswordService;
import com.ttn.ecommerceProject.ttnEcommerceProject.service.authService.LoginService;
import com.ttn.ecommerceProject.ttnEcommerceProject.service.authService.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final LoginService loginService;
   // private final ForgetPasswordRequest forgetPasswordRequest;
    private final ForgetPasswordService forgetPasswordService;
    //private final CustomerAuthService customerAuthService;
    private final UserService userService

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto req) {
        return ResponseEntity.ok(userService.login(req));
    }

    @GetMapping("/test")
    public String test(){
        return "Test accepted!";
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(){
        return ResponseEntity.ok(new ApiResponse("You logged out successfuly"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest req) {
        userService.resetPassword(req);
        return ResponseEntity.ok(new ApiResponse("Password reset successful"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgetPasswordRequest req) {
        userService.forgetPassword(req);
        return ResponseEntity.ok(new ApiResponse("Password reset link generated"));
    }

    @GetMapping("/activate")
    public ResponseEntity<ApiResponse> activate(@RequestParam String token) {
        userService.activate(token);
        return ResponseEntity.ok(new ApiResponse("Account activated successfully"));
    }

    @PostMapping("/resend-activation")
    public ResponseEntity<ApiResponse> resend(@Valid @RequestBody ResendActivationRequest req) {
        userService.resendActivation(req.getEmail());
        return ResponseEntity.ok(new ApiResponse("Activation link resent."));
    }



}
