package com.ttn.ecommerceProject.ttnEcommerceProject.repo;

import com.ttn.ecommerceProject.ttnEcommerceProject.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepo extends JpaRepository<Customer , UUID> {

}
