package com.ttn.ecommerceProject.ttnEcommerceProject.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Size(min = 2, max = 50 , message = "first name must be between 2 and 5 characters")
    @NotBlank(message = "firstName required")
    @Column(name = "FirstName" , nullable = false)
    private String firstName;

    @Size(max = 50, message = "Middle name must not exceed 50 characters")
    private String middleName;


    @NotBlank(message = "Last name is required")
    @Column(name="LastName" , nullable = false)
    private String lastName;

    @NotBlank(message = "email is required")
    @Email(message = "invalid email format")
    @Column(name = "Email" , nullable = false , unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;


    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(nullable = false)
    private boolean isActive = false;

    @Column(nullable = false)
    private boolean isExpired = false;

    @Column(nullable = false)
    private boolean isLocked = false;

    private Integer invalidAttemptCount = 0 ;
    private LocalDateTime passwordUpdateDate;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_role" , joinColumns = @JoinColumn(name="user_id") ,
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Customer customer;

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Seller seller;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Address> addresses = new HashSet<>();


}
