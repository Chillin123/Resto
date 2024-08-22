package com.base.resto.services;

import com.base.resto.models.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface CustomUserDetailsService extends UserDetailsService {

    CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}