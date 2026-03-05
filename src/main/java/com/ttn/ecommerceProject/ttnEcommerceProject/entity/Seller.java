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
@Table(name = "sellers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seller {

    @Id
    @Column(name = "user_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @NotBlank(message = "company name is required")
    @Size(min = 2 , max = 100 , message = "company name must be 2 to 100 characters")
    @Column(nullable = false)
    private String companyName;


    @NotBlank(message = "Company contact is required")
    @Pattern(
            regexp = "^[0-9]{10,15}$",
            message = "Company contact must contain 10–15 digits"
    )
    @Column(nullable = false)
    private String companyContact;

    @NotBlank(message = "GST number is required")
    @Pattern(
            regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[A-Z0-9]{1}Z[A-Z0-9]{1}$",
            message = "Invalid GST number format"
    )
    @Column(nullable = false, unique = true, length = 15)
    private String gst;



    //check it later
    @Column(nullable = false)
    private boolean isApproved = false;
}
