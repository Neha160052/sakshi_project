package com.ttn.ecommerceProject.ttnEcommerceProject.dto.registerDto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CustomerRegisterRequest {


    @NotBlank(message = "First name required")
    @Size(min = 2, max = 50)
    private String firstName;

    @Size(max = 50)
    private String middleName;

    @NotBlank(message = "Last name required")
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank(message = "Email required")
    @Email(message = "Invalid email")
    @Size(max=100)
    private String email;

    @NotBlank(message = "Phone number required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    private String contact;


    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$",
            message = "Password must be 8-20 characters and include uppercase, lowercase, digit and special character"
    )
    private String password;


    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    @Valid
    private Set<AddressRequest> addresses;

}
