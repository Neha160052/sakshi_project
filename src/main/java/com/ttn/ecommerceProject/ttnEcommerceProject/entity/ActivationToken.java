package com.ttn.ecommerceProject.ttnEcommerceProject.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="activationTokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , unique = true , length = 64)
    private String token;


}
