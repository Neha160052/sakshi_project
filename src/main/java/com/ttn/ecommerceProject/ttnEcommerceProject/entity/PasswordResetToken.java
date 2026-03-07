package com.ttn.ecommerceProject.ttnEcommerceProject.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Table(name="password_reset_token")
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , unique = true , length = 100)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER , optional = false)
    @JoinColumn(name="user_id" , nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiryDateTime;
}
