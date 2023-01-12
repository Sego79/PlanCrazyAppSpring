package com.plancrazyappfrontofficespring.repository;

import com.plancrazyappfrontofficespring.model.AppUser;
import com.plancrazyappfrontofficespring.model.Task;
import com.plancrazyappfrontofficespring.model.UserTaskAssociation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class UserTaskAssociationRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserTaskAssociationRepository userTaskAssociationRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Test
    void given__2usersAnd2tasks__when__findUserTaskAssociationByAppUserAndTask__then__returnMatchingAssociation() {
        // given
        AppUser appUser1 = new AppUser("nickname1", "firstName1", "lastName1",
                "address1", 46800, "city1", "064781776", "mail1@mail.fr",
                "123");
        AppUser appUser2 = new AppUser( "nickname2", "firstName2", "lastName2",
                "address2", 46800, "city2", "0647626", "mail2@mail.fr",
                "123");
        Task task1 = new Task("Titre1", "Description1", "Location1",
                LocalDate.of(2022, 12, 20), LocalTime.of(10,0),
                LocalDate.of(2022,12,20), LocalTime.of(11, 0), true);
        Task task2 = new Task("Titre2", "Description2", "Location2",
                LocalDate.of(2022, 12, 20), LocalTime.of(10,0),
                LocalDate.of(2022,12,20), LocalTime.of(11, 0), true);
        entityManager.persist(appUser1);
        entityManager.persist(task1);
        entityManager.persist(appUser2);
        entityManager.persist(task2);
        entityManager.persist(new UserTaskAssociation(appUser1, task1, false));
        entityManager.persist(new UserTaskAssociation(appUser2, task2, false));

        // when
        UserTaskAssociation result = userTaskAssociationRepository.findUserTaskAssociationByAppUserAndTask(appUser1, task1);

        // then
        assertThat(result.getUser()).isEqualTo(appUser1);
    }
}