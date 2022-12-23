package com.plancrazyappfrontofficespring.repository;

import com.plancrazyappfrontofficespring.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
}
