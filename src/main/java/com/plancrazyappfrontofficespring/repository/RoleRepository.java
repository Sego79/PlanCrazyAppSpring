package com.plancrazyappfrontofficespring.repository;

import com.plancrazyappfrontofficespring.model.Role;
import com.plancrazyappfrontofficespring.model.RoleEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(RoleEnum role);
}
