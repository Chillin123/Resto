package com.base.resto.controller;

import com.base.resto.dto.request.UpdateAuthorityAdminRequest;
import com.base.resto.dto.request.UpdateAuthorityRequest;
import com.base.resto.dto.response.UpdateAuthorityResponse;
import com.base.resto.services.RedisCacheService;
import com.base.resto.services.UpdateAuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class AuthorityController {
    private final UpdateAuthorityService updateAuthorityService;
    private final RedisCacheService redisCacheService;

    @PostMapping("/updateAuthority")
    public ResponseEntity<UpdateAuthorityResponse> updateAuthority(@RequestBody UpdateAuthorityRequest request) {
        try {
            boolean isValid = updateAuthorityService.matchAuthorityCode(request.getAuthorityCode());
            String secureCode = new java.security.SecureRandom().ints(8, 0, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".length())
                    .mapToObj("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"::charAt)
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
            if (isValid) {
                updateAuthorityService.cacheAuthorityKey(request.getPhone(), secureCode);
                return ResponseEntity.ok(UpdateAuthorityResponse.builder().message("Success").code(secureCode).build());
            }
            return ResponseEntity.badRequest().body(UpdateAuthorityResponse.builder().message("Failed").build());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(UpdateAuthorityResponse.builder().message("Failed").build());
        }
    }

    @PostMapping("/admin/updateAuthprity")
    public  ResponseEntity<String> updateAuthorityAdmin(@RequestBody UpdateAuthorityAdminRequest request) {
        try {
            String secureCode = (String) redisCacheService.getData(request.getPhone());
            updateAuthorityService.updateAuthority(request.getPhone(), secureCode, request.getRole());
            return ResponseEntity.ok("Success");
        }catch (Exception e) {
            return ResponseEntity.status(403).body(e.toString());
        }
    }
}
