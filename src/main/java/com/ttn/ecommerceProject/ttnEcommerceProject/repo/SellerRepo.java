package com.ttn.ecommerceProject.ttnEcommerceProject.repo;

import com.ttn.ecommerceProject.ttnEcommerceProject.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SellerRepo extends JpaRepository<Seller , UUID> {
    Optional<Seller> findByUserId(UUID userId);
}
