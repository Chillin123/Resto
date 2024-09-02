package com.base.resto.services;

import com.base.resto.controller.WebSocketController;
import com.base.resto.enums.Role;
import com.base.resto.models.AuthorityKey;
import com.base.resto.models.User;
import com.base.resto.repositories.AuthorityKeyRepository;
import com.base.resto.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UpdateAuthorityService {

    private final AuthorityKeyRepository authorityKeyRepository;
    private final UserRepository userRepository;
    private final WebSocketController webSocketController;
    private final RedisCacheService redisCacheService;

    public boolean matchAuthorityCode(String authorityCode){
        Optional<AuthorityKey> fetchedAuthorityKey = authorityKeyRepository.findById(1);
        if(fetchedAuthorityKey.isPresent()){
            AuthorityKey authorityKey = fetchedAuthorityKey.get();
            return authorityKey.getKey().equals(authorityCode);
        }
        return false;
    }

    public void cacheAuthorityKey(String phone, String authorityCode){
        redisCacheService.cacheData(phone, authorityCode, 300, TimeUnit.SECONDS);
        webSocketController.sendUpdateAuthorityRequestToAdmin("Update Authority");
    }

    public void updateAuthority(String phone, String secureCode, String role) {
        Optional<User> user = userRepository.findByPhone(phone);
        if(user.isPresent()){
            User currentUser = user.get();
            currentUser.setRole(Role.valueOf(role));
            userRepository.save(currentUser);
        }
        String message = "Your authority has been updated to: " + role;
        webSocketController.sendAuthorityUpdateNotification(secureCode, message);
    }
}
