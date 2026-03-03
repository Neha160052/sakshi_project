package com.ttn.ecommerceProject.ttnEcommerceProject.repo;

import com.ttn.ecommerceProject.ttnEcommerceProject.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomRepo extends JpaRepository<Customer , Long> {
}
