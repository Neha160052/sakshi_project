package com.ttn.ecommerceProject.ttnEcommerceProject.controller;


import com.ttn.ecommerceProject.ttnEcommerceProject.dto.ApiResponse;
import com.ttn.ecommerceProject.ttnEcommerceProject.dto.registerDto.CustomerRegisterRequest;
import com.ttn.ecommerceProject.ttnEcommerceProject.dto.registerDto.ResendActivationRequest;
import com.ttn.ecommerceProject.ttnEcommerceProject.service.authService.CustomerAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/customer")
@RequiredArgsConstructor
public class CustomerAuthController {
    private final CustomerAuthService customerAuthService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody CustomerRegisterRequest req){
        customerAuthService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("registration successfull. check your email to activate your account."));
    }

    @PutMapping("/activate")
    public ResponseEntity<ApiResponse> activate(@RequestParam String token){
        customerAuthService.activate(token);
        return ResponseEntity.ok(new ApiResponse("Account activates successfully"));
    }

    @PostMapping("/resend-activation")
    public ResponseEntity<ApiResponse> resend(@Valid @RequestBody ResendActivationRequest req){
        customerAuthService.resendActivation(req.getEmail());
        return ResponseEntity.ok(new ApiResponse("Activation link resent."));
    }
}
