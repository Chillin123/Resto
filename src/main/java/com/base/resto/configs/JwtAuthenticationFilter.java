package com.base.resto.configs;

import com.base.resto.enums.AuthorityLevel;
import com.base.resto.enums.Role;
import com.base.resto.models.CustomUserDetails;
import com.base.resto.services.CustomUserDetailsService;
import com.base.resto.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwt;
        final String phone;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            phone = jwtService.extractUsername(jwt);
            if(phone != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(phone);
                if(jwtService.validateToken(jwt, customUserDetails)) {
                    if(!isAuthorized(jwt, request)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Insufficient authority for the request");
                        return;
                    }
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            customUserDetails,
                            null,
                            customUserDetails.getAuthorities()
                    );
                    authentication.setDetails(
                            new WebAuthenticationDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        }else{
            filterChain.doFilter(request, response);
        }
    }

    protected boolean isAuthorized(String jwt, @NonNull HttpServletRequest request) {
        Role role = jwtService.extractRole(jwt);
        role = role == null ? Role.CUSTOMER : role;
        String requestUri = request.getRequestURI();
        AuthorityLevel requiredAuthority = EndpointSecurityConfig.getRequiredAuthorityForEndpoint(requestUri);
        System.out.println(requiredAuthority);
        System.out.println(role);
        return role == Role.ADMIN || (role == Role.EMPLOYEE && requiredAuthority != AuthorityLevel.ADMIN)
                || (role == Role.CUSTOMER && requiredAuthority == AuthorityLevel.CUSTOMER);
    }
}
