package com.base.resto.services;

import com.base.resto.dto.request.AuthenticationRequest;
import com.base.resto.dto.request.RegisterRequest;
import com.base.resto.dto.response.AuthenticationResponse;
import com.base.resto.models.User;
import com.base.resto.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user = User.builder().name(registerRequest.getName()).phone(registerRequest.getPhone()).password(passwordEncoder.encode(registerRequest.getPassword())).build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getPhone(),
                        authenticationRequest.getPassword()
                )
        );
        var user = userRepository.findByPhone(authenticationRequest.getPhone()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
