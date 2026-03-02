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

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @NotBlank(message = "contact is required")
    @Column(nullable = false)
    @Size(min = 10, max = 15, message = "Contact must be 10 to 15 digits")
    @Pattern(regexp = "^[0-9]+$", message = "Contact must contain digits only")
    private String contact;
}
