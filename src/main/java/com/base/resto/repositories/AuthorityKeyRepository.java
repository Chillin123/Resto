package com.base.resto.repositories;

import com.base.resto.models.AuthorityKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityKeyRepository extends JpaRepository<AuthorityKey, Integer> {
    Optional<AuthorityKey> findById(int id);
}
