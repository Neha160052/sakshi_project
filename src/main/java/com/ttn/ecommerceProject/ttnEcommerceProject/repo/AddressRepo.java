package com.ttn.ecommerceProject.ttnEcommerceProject.repo;

import com.ttn.ecommerceProject.ttnEcommerceProject.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepo extends JpaRepository<Address , UUID> {
}
