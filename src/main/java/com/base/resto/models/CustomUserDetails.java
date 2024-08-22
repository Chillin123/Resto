package com.base.resto.models;

import com.base.resto.enums.Role;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {
    Role getRole();
}
