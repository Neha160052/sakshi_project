package com.ttn.ecommerceProject.ttnEcommerceProject.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="activationTokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false , unique = true , length = 64)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name="user_id" , nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiryDateTime;


}
