package com.plancrazyappfrontofficespring.controller.dto;

import com.plancrazyappfrontofficespring.model.Task;

import java.time.LocalDate;
import java.time.LocalTime;

public class TaskDto {

    private Long taskId;

    private String taskTitle;

    private String description;

    private String location;

    private LocalDate startingDate;

    private LocalTime startingHour;

    private LocalDate endingDate;

    private LocalTime endingHour;

    private Boolean isPrivate;

    private String ownerEmail;

    public TaskDto() {
    }

    public TaskDto(Task task) {
        this.setTaskId(task.getTaskId());
        this.setTaskTitle(task.getTaskTitle());
        this.setDescription(task.getDescription());
        this.setLocation(task.getLocation());
        this.setStartingDate(task.getStartingDate());
        this.setStartingHour(task.getStartingHour());
        this.setEndingDate(task.getEndingDate());
        this.setEndingHour(task.getEndingHour());
        this.setPrivate(task.getPrivate());
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

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "taskId=" + taskId +
                ", taskTitle='" + taskTitle + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", startingDate=" + startingDate +
                ", startingHour=" + startingHour +
                ", endingDate=" + endingDate +
                ", endingHour=" + endingHour +
                ", isPrivate=" + isPrivate +
                '}';
    }
}
