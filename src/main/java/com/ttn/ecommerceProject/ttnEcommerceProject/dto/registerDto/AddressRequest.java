package com.ttn.ecommerceProject.ttnEcommerceProject.dto.registerDto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {

    @NotBlank(message = "Addess line is required")
    @Size(min=5 , max = 255 , message = "Address line must be 5 to 255 characters")
    private String addressLine;

    @NotBlank(message = "city is required")
    @Size(min = 2, max = 100, message = "City must be 2 to 100 characters")
    private String city;

    @NotBlank(message = "state is required")
    @Size(min = 2, max = 100, message = "State must be 2 to 100 characters")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 100, message = "Country must be 2 to 100 characters")
    private String country;

    @NotBlank(message = "Zip code is required")
    @Pattern(regexp = "^[0-9]{6}$" , message = "zip code must be 6 digits")
    private String zipCode;


}
