package com.ttn.ecommerceProject.ttnEcommerceProject.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Address line is required")
    @Size(min = 5 ,  message = "Address line must be atleast 5 characters")
    @Column(nullable = false)
    private String addressLine;


    @NotBlank(message = "City is required")
    @Size(min = 2, message = "City must be 2 to 100 characters")
    @Column(nullable = false)
    private String city;


    @NotBlank(message = "State is required")
    @Size(min = 2 , message = "State must be 2 to 100 characters")
    @Column(nullable = false)
    private String state;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 100, message = "Country must be 2 to 100 characters")
    @Column(nullable = false)
    private String country;


    @NotBlank(message = "Zip code is required")
    @Pattern(regexp = "^[0-9]{6}$", message = "Zip code must be 6 digits")
    @Column(nullable = false)
    private String zipCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;


}
