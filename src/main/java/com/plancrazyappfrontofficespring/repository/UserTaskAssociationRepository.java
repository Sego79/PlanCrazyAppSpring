package com.plancrazyappfrontofficespring.repository;

import com.plancrazyappfrontofficespring.model.AppUser;
import com.plancrazyappfrontofficespring.model.Task;
import com.plancrazyappfrontofficespring.model.UserTaskAssociation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTaskAssociationRepository  extends JpaRepository<UserTaskAssociation, Long> {
    void deleteByTaskAndAppUser(Task task, AppUser user);

    void deleteAllByTask(Task task);
}
