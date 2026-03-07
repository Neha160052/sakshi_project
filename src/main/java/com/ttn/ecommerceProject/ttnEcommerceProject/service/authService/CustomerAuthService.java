package com.ttn.ecommerceProject.ttnEcommerceProject.service.authService;

import com.ttn.ecommerceProject.ttnEcommerceProject.dto.registerDto.AddressRequest;
import com.ttn.ecommerceProject.ttnEcommerceProject.dto.registerDto.CustomerRegisterRequest;
import com.ttn.ecommerceProject.ttnEcommerceProject.entity.*;
import com.ttn.ecommerceProject.ttnEcommerceProject.exception.ResourceAlreadyExistsException;
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
    private final EmailService emailService;

    @Transactional
    public String register(CustomerRegisterRequest req) {

        //        if(userRepo.existsByEmail(req.getEmail())){
        //            throw new RuntimeException("Email already registered");
        //        }

        User exist = userRepo.findByEmail(req.getEmail()).orElse(null);

        if (exist != null) {

            if (!passwordService.matches(req.getPassword(), exist.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            if (customerRepo.findById(exist.getId()).isPresent()) {
                // throw new RuntimeException("Customer already exist");
                throw new ResourceAlreadyExistsException("Customer already exists with this email");
            }

            //            if(exist.isActive()){
            //                return "Email already registered. You can login" ;
            if (!exist.isActive()) {
                ActivationToken token = activationService.createToken(exist);

                emailService.sendActivationEmail(
                        exist.getEmail(),
                        "Account Activation",
                        token.getToken()
                );

                return "Account not activated. Activation link resent.";
            }

            boolean alreadyHasCustomerRole = exist.getRoles().stream()
                    .anyMatch(r -> "CUSTOMER".equalsIgnoreCase(r.getAuthority()));

            if (!alreadyHasCustomerRole) {
                Role customerRole = roleRepo.findByAuthority("CUSTOMER")
                        .orElseThrow(() -> new RuntimeException("customer role not found"));
                exist.getRoles().add(customerRole);
                userRepo.save(exist);
            }

            Customer customer = new Customer();
            customer.setUser(exist);
            customer.setContact(req.getContact());
            customerRepo.save(customer);

            if (req.getAddresses() != null && !req.getAddresses().isEmpty()) {
                for (AddressRequest a : req.getAddresses()) {
                    Address address = new Address();
                    address.setUser(exist);
                    address.setAddressLine(a.getAddressLine());
                    address.setCity(a.getCity());
                    address.setState(a.getState());
                    address.setCountry(a.getCountry());
                    address.setZipCode(a.getZipCode());
                    addressRepo.save(address);
                }
            }

            return "Customer registered successfully";
        }

        passwordService.passwordMatch(req.getPassword(), req.getConfirmPassword());

        Role customerRole = roleRepo.findByAuthority("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("customer role not found"));

        User user = new User();

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

        if (req.getAddresses() != null && !req.getAddresses().isEmpty()) {
            for (AddressRequest a : req.getAddresses()) {
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


        // ActivationToken token = activationService.createToken(exist);
        // String link = "Activate your account using this link:\n\n" + token ;
        // emailService.sendActivationEmail(exist.getEmail(), "Account Activation", link);


        emailService.sendActivationEmail(
                user.getEmail(),
                "Account Activation",
                token.getToken()
        );

        return "Registration successful. Please check your email to activate your account";
    }

    
    //
    // @Transactional
    // public void activate(String tokenValue){
    //     ActivationToken token = activationService.validateToken(tokenValue);
    //     User user = token.getUser();
    //     user.setActive(true);
    //     userRepo.save(user);
    //     activationService.deleteToken(token);
    // }


    //
    // @Transactional
    // public void resendActivation(String email){
    //     User user = userRepo.findByEmail(email)
    //             .orElseThrow(()->new RuntimeException("User not found"));
    //
    //     if(user.isActive()){
    //         throw new RuntimeException("Account already activated");
    //     }
    //
    //     ActivationToken token = activationService.createToken(user);
    //     emailService.sendActivationEmail(
    //             user.getEmail(),
    //             "Account Activation",
    //              token.getToken()
    //     );
    // }
}