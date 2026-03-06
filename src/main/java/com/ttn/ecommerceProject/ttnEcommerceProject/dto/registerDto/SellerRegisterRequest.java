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
public class SellerRegisterRequest {

    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;

    @Size(max = 50)
    private String middleName;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$",
            message = "Password must be 8-20 and include uppercase, lowercase, digit, special char"
    )
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank(message = "GST is required")
    @Size(min = 5, max = 20, message = "GST length seems invalid")
    private String gst;

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 100)
    private String companyName;

    @NotBlank(message = "Company address is required")
    @Size(min = 5, max = 255)
    private String companyAddress;

    @NotBlank(message = "Company contact is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Company contact must be 10-15 digits")
    private String companyContact;

    @Valid
    private Set<AddressRequest> addresses;

}


