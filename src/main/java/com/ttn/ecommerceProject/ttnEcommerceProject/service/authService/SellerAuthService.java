package com.ttn.ecommerceProject.ttnEcommerceProject.service.authService;


import com.ttn.ecommerceProject.ttnEcommerceProject.dto.registerDto.AddressRequest;
import com.ttn.ecommerceProject.ttnEcommerceProject.dto.registerDto.SellerRegisterRequest;
import com.ttn.ecommerceProject.ttnEcommerceProject.entity.*;
import com.ttn.ecommerceProject.ttnEcommerceProject.repo.AddressRepo;
import com.ttn.ecommerceProject.ttnEcommerceProject.repo.RoleRepo;
import com.ttn.ecommerceProject.ttnEcommerceProject.repo.SellerRepo;
import com.ttn.ecommerceProject.ttnEcommerceProject.repo.UserRepo;
import com.ttn.ecommerceProject.ttnEcommerceProject.service.emailService.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerAuthService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final SellerRepo sellerRepo;
    private final AddressRepo addressRepo;
    private final ActivationService activationService;
    private final PasswordService passwordService;
    private final EmailService emailService;

    @Transactional
    public void register(SellerRegisterRequest req) {
        Role sellerRole = roleRepo.findByAuthority("SELLER")
                .orElseThrow(() -> new RuntimeException("Seller role not found"));

        User user = userRepo.findByEmail(req.getEmail()).orElse(null);

        if (user != null) {
            if (!passwordService.matches(req.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            //            if (sellerRepo.findByUserId(user.getId()).isPresent()) {
            //                throw new RuntimeException("seller already registerd");
            //            }

            Seller seller = sellerRepo.findByUserId(user.getId()).orElse(null);

            if (seller != null) {

                if (seller.getStatus() == SellerStatus.PENDING) {
                    throw new RuntimeException("Seller request already pending approval");
                }

                if (seller.getStatus() == SellerStatus.APPROVED) {
                    throw new RuntimeException("Seller already registered");
                }

                if (seller.getStatus() == SellerStatus.REJECTED) {
                    throw new RuntimeException("Seller request rejected. Please contact admin.");
                }
            }

            if (!user.isActive()) {
                ActivationToken token = activationService.createToken(user);


                // String link = "Activate your account using this link:\n\n" + token ;
                // emailService.sendActivationEmail(user.getEmail(), "Account Activation", link);

                // CORRECT VERSION
                emailService.sendActivationEmail(
                        user.getEmail(),
                        "Account Activation",
                        token.getToken()
                );

                throw new RuntimeException("Account not activated. Activation link resent");
            }


            // user.getRoles().add(sellerRole);
            // userRepo.save(user);

            // CORRECT VERSION
            boolean alreadyHasSellerRole = user.getRoles().stream()
                    .anyMatch(r -> "SELLER".equalsIgnoreCase(r.getAuthority()));

            if (!alreadyHasSellerRole) {
                user.getRoles().add(sellerRole);
                userRepo.save(user);
            }

        } else {
            passwordService.passwordMatch(req.getPassword(), req.getConfirmPassword());

            user = new User();
            //  user.setId(UUID.randomUUID());
            user.setFirstName(req.getFirstName());
            user.setMiddleName(req.getMiddleName());
            user.setLastName(req.getLastName());
            user.setEmail(req.getEmail());
            user.setPassword(passwordService.encode(req.getPassword()));

            user.setActive(false);
            user.setLocked(false);
            user.setDeleted(false);
            user.setExpired(false);

            user.getRoles().add(sellerRole);
            userRepo.save(user);

            ActivationToken token = activationService.createToken(user);


            // String link = "Activate your account using this link:\n\n" + token ;
            // emailService.sendActivationEmail(user.getEmail(), "Account Activation", link);


            emailService.sendActivationEmail(
                    user.getEmail(),
                    "Account Activation",
                    token.getToken()
            );
        }

        Seller seller = new Seller();
        seller.setUser(user);
        seller.setGst(req.getGst());
        seller.setCompanyName(req.getCompanyName());
        seller.setCompanyContact(req.getCompanyContact());
        seller.setStatus(SellerStatus.PENDING);

        sellerRepo.save(seller);

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


    }

    }




