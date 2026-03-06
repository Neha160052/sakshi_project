package com.ttn.ecommerceProject.ttnEcommerceProject.controller;


import com.ttn.ecommerceProject.ttnEcommerceProject.dto.ApiResponse;
import com.ttn.ecommerceProject.ttnEcommerceProject.dto.registerDto.SellerRegisterRequest;
import com.ttn.ecommerceProject.ttnEcommerceProject.service.authService.SellerAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/seller")
@RequiredArgsConstructor
public class SellerAuthController {

    private final SellerAuthService sellerAuthService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody SellerRegisterRequest req){
        sellerAuthService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Seller registered successfully . please check email for activation. Status: waiting for approval."));

    }

}
