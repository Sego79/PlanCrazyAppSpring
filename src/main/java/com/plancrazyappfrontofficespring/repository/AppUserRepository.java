package com.plancrazyappfrontofficespring.repository;

import com.plancrazyappfrontofficespring.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository  extends JpaRepository<AppUser, Long> {
}
