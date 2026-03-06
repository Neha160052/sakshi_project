package com.ttn.ecommerceProject.ttnEcommerceProject.filter;

import com.ttn.ecommerceProject.ttnEcommerceProject.service.security.CustomUserDetailService;
import com.ttn.ecommerceProject.ttnEcommerceProject.service.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JwtService jwt;
    private final CustomUserDetailService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {

        if (request.getRequestURI().equals("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authToken = authorizationHeader.substring(7);

        try {
            if (!authToken.isEmpty() && jwt.validateToken(authToken)) {

                String username = jwt.extractUsername(authToken);

                if (username != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwt.isTokenValid(authToken, userDetails.getUsername())) {

                        UsernamePasswordAuthenticationToken authenticated =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );

                        SecurityContextHolder.getContext().setAuthentication(authenticated);

                        log.debug("JWT authentication successful for user: {}", username);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Exception in JWT filter:", ex);
        }

        filterChain.doFilter(request, response);
    }
}