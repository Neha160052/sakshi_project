package com.ttn.ecommerceProject.ttnEcommerceProject.dto.forgetPasswordDto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgetPasswordRequest {
    @NotBlank(message = "Email is required")
    @Email(message ="Enter valid email")
    public String email;

}
