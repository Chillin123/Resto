package com.base.resto.configs;

import com.base.resto.enums.AuthorityLevel;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class EndpointSecurityConfig {
    private static final Map<String, AuthorityLevel> endpointAuthorityMap = new HashMap<>();

    static {
        endpointAuthorityMap.put("/admin/.*", AuthorityLevel.ADMIN);
        endpointAuthorityMap.put("/employee/.*", AuthorityLevel.EMPLOYEE);
    }

    public static AuthorityLevel getRequiredAuthorityForEndpoint(String endpoint) {
        return endpointAuthorityMap.entrySet().stream()
                .filter(entry -> {
                    Pattern pattern = Pattern.compile(entry.getKey());
                    Matcher matcher = pattern.matcher(endpoint);
                    return matcher.matches();
                })
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(AuthorityLevel.CUSTOMER);
    }
}
