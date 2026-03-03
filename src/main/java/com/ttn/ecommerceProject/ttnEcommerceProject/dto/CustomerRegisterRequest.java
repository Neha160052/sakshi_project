package com.ttn.ecommerceProject.ttnEcommerceProject.dto;


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
    private String email;

    @NotBlank(message = "Phone number required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    private String phoneNumber;


    private String password;

    private String ConfirmPassword;

    private Set<AddressRequest> addresses;

}
