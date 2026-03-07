package com.ttn.ecommerceProject.ttnEcommerceProject.repo;

import com.ttn.ecommerceProject.ttnEcommerceProject.entity.PasswordResetToken;
import com.ttn.ecommerceProject.ttnEcommerceProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken , Long> {

    Optional<PasswordResetToken>findByToken(String token);
    Optional<PasswordResetToken>findByUser(User user);
    void deleteByUser(User user);
}
