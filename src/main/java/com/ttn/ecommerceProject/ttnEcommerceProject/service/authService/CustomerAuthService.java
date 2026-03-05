package com.ttn.ecommerceProject.ttnEcommerceProject.service.authService;

import com.ttn.ecommerceProject.ttnEcommerceProject.dto.AddressRequest;
import com.ttn.ecommerceProject.ttnEcommerceProject.dto.CustomerRegisterRequest;
import com.ttn.ecommerceProject.ttnEcommerceProject.entity.*;
import com.ttn.ecommerceProject.ttnEcommerceProject.repo.AddressRepo;
import com.ttn.ecommerceProject.ttnEcommerceProject.repo.CustomerRepo;
import com.ttn.ecommerceProject.ttnEcommerceProject.repo.RoleRepo;
import com.ttn.ecommerceProject.ttnEcommerceProject.repo.UserRepo;
import com.ttn.ecommerceProject.ttnEcommerceProject.service.emailService.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerAuthService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final CustomerRepo customerRepo;
    private final AddressRepo addressRepo;
    private final ActivationService activationService;
    private final PasswordService passwordService;
    private  final EmailService emailService;


    @Transactional
    public void register(CustomerRegisterRequest req){
        if(userRepo.existsByEmail(req.getEmail())){
            throw new RuntimeException("Email already registered");
        }
        passwordService.passwordMatch(req.getPassword() , req.getConfirmPassword());

        Role customerRole = roleRepo.findByAuthority("CUSTOMER")
                .orElseThrow(()-> new RuntimeException("customer role not found"));

        User user = new User();
        user.setId(java.util.UUID.randomUUID());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setMiddleName(req.getMiddleName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordService.encode(req.getPassword()));

        user.setActive(false);
        user.setLocked(false);
        user.setDeleted(false);

        user.getRoles().add(customerRole);
        userRepo.save(user);


        Customer customer = new Customer();
        customer.setUser(user);
        customer.setContact(req.getContact());
        customerRepo.save(customer);

        if(req.getAddresses() !=null && !req.getAddresses().isEmpty()){
            for(AddressRequest a : req.getAddresses()){
                Address address = new Address();
                address.setUser(user);
                address.setAddressLine(a.getAddressLine());
                address.setCity(a.getCity());
                address.setState(a.getState());
                address.setCountry(a.getCountry());
                address.setZipCode(a.getZipCode());

                addressRepo.save(address);
            }

        }

        ActivationToken token = activationService.createToken(user);
        String linkToken = "http://localhost:8080/auth/customer/activate?token=" + token.getToken();
        emailService.sendActivationMail(user.getEmail() , linkToken);

    }

    @Transactional
    public void activate(String tokenValue){
        ActivationToken token = activationService.validateToken(tokenValue);
        User user = token.getUser();
        user.setActive(true);
        userRepo.save(user);
        activationService.deleteToken(token);
    }

    public void resendActivation(String email){
        User user = userRepo.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));

        if(user.isActive()){
            throw new RuntimeException("Account already activated");
        }

        ActivationToken token = activationService.createToken(user);
        String linkToken = "http://localhost:8080/auth/customer/activate?token=" + token.getToken();
        emailService.sendActivationMail(user.getEmail() , linkToken);
    }





}
