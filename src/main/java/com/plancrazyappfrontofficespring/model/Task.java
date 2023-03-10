package com.plancrazyappfrontofficespring.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="task_id", nullable = false, unique = true)
    private Long taskId;

    @Column(name="task_title", nullable = false)
    private String taskTitle;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(name="starting_date", nullable = false)
    private LocalDate startingDate;

    @Column(name="starting_hour", nullable = false)
    private LocalTime startingHour;

    @Column(name="ending_date", nullable = false)
    private LocalDate endingDate;

    @Column(name="ending_hour", nullable = false)
    private LocalTime endingHour;

    @Column(name="is_Private", nullable = false)
    private Boolean isPrivate;

    @OneToMany
    private List<Category> categoryList = new ArrayList<>();

    @OneToMany
    private List<ProgressStatus> progressStatusList = new ArrayList<>();

    @OneToMany(mappedBy = "task")
    private List<UserTaskAssociation> UserTaskAssociationList = new ArrayList<>();

    public Task() {

    }

    public Task(String taskTitle, String description, String location, LocalDate startingDate, LocalTime startingHour, LocalDate endingDate, LocalTime endingHour, Boolean isPrivate) {
        this.taskTitle = taskTitle;
        this.description = description;
        this.location = location;
        this.startingDate = startingDate;
        this.startingHour = startingHour;
        this.endingDate = endingDate;
        this.endingHour = endingHour;
        this.isPrivate = isPrivate;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public LocalTime getStartingHour() {
        return startingHour;
    }

    public void setStartingHour(LocalTime startingHour) {
        this.startingHour = startingHour;
    }

    public LocalDate getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(LocalDate endingDate) {
        this.endingDate = endingDate;
    }

    public LocalTime getEndingHour() {
        return endingHour;
    }

    public void setEndingHour(LocalTime endingHour) {
        this.endingHour = endingHour;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<ProgressStatus> getProgressStatusList() {
        return progressStatusList;
    }

    public void setProgressStatusList(List<ProgressStatus> progressStatusList) {
        this.progressStatusList = progressStatusList;
    }

    public List<UserTaskAssociation> getAssociationList() {
        return UserTaskAssociationList;
    }

    public void setAssociationList(List<UserTaskAssociation> associationList) {
        this.UserTaskAssociationList = associationList;
    }
}

