package com.plancrazyappfrontofficespring.repository;

import com.plancrazyappfrontofficespring.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByEmail(String email);

    Optional<AppUser> findUserByEmail(String email);

    void deleteByEmail(String email);
}
