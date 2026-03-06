package com.ttn.ecommerceProject.ttnEcommerceProject.dto.loginDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDto {

    private String token;
    private String email;
    private Set<String> roles;
}
