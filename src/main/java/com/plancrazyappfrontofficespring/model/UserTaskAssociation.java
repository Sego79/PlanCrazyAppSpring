package com.plancrazyappfrontofficespring.model;

import jakarta.persistence.*;

@Entity
public class UserTaskAssociation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_task_association_id", nullable = false, unique = true)
    private Long userTaskAssociationId;

    private boolean owner;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "task_id")
    Task task;

    public UserTaskAssociation() {

    }

    public UserTaskAssociation(AppUser appUser, Task task, boolean owner) {
        this.appUser = appUser;
        this.task = task;
        this.owner = owner;
    }

    public Long getUserTaskAssociationId() {
        return userTaskAssociationId;
    }

    public void setUserTaskAssociationId(Long userTaskAssociationId) {
        this.userTaskAssociationId = userTaskAssociationId;
    }

    public AppUser getUser() {
        return appUser;
    }

    public void setUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
