package com.plancrazyappfrontofficespring.service;

import com.plancrazyappfrontofficespring.controller.dto.AppUserDto;
import com.plancrazyappfrontofficespring.controller.dto.TaskDto;
import com.plancrazyappfrontofficespring.model.AppUser;
import com.plancrazyappfrontofficespring.model.Task;
import com.plancrazyappfrontofficespring.model.UserTaskAssociation;
import com.plancrazyappfrontofficespring.repository.AppUserRepository;
import com.plancrazyappfrontofficespring.repository.TaskRepository;
import com.plancrazyappfrontofficespring.repository.UserTaskAssociationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

//todo : mieux gérer les exceptions : envoyer des user not found exception, etc. par exemple :)

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private UserTaskAssociationRepository userTaskAssociationRepository;


    public TaskDto fetchById(Long id) throws Exception {
        Optional<Task> taskOpt = taskRepository.findById(id);
        String ownerEmail = getTaskOwnerEmail(taskOpt.orElseThrow(() -> new Exception()));
        TaskDto taskDto = new TaskDto(taskOpt.orElseThrow(() -> new Exception()));
        taskDto.setOwnerEmail(ownerEmail);
        return taskDto;
    }

    public List<TaskDto> fetchTaskOfUser(AppUserDto connectedUser) {
        return userTaskAssociationRepository.findAll().stream()
                .filter(userTaskAssociation -> userTaskAssociation.getUser().getAppUserId() == connectedUser.getAppUserId())
                .map(userTaskAssociation -> {
                    try {
                        return taskDtoWithOwnerEmailFromAssociation(userTaskAssociation);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private static TaskDto taskDtoWithOwnerEmailFromAssociation(UserTaskAssociation userTaskAssociation) throws Exception {
        TaskDto taskDto = new TaskDto(userTaskAssociation.getTask());
        if (userTaskAssociation.isOwner()){
            taskDto.setOwnerEmail(userTaskAssociation.getUser().getEmail());
        } else {
            taskDto.setOwnerEmail(getTaskOwnerEmail(userTaskAssociation.getTask()));
        }
        return taskDto;
    }

    private static String getTaskOwnerEmail(Task task) throws Exception {
        return task.getAssociationList().stream()
                .filter(asso -> asso.isOwner())
                .map(asso -> asso.getUser().getEmail())
                .findFirst()
                .orElseThrow(() -> new Exception());
    }

    @Transactional
    public TaskDto addTask(TaskDto dto, AppUserDto appUserDto) {
        Task task = new Task(
                dto.getTaskTitle(),
                dto.getDescription(),
                dto.getLocation(),
                dto.getStartingDate(),
                dto.getStartingHour(),
                dto.getEndingDate(),
                dto.getEndingHour(),
                dto.getPrivate()
        );

        AppUser connectedAppUser = appUserRepository.findById(appUserDto.getAppUserId()).get();//todo : meilleure gestion de l'optionnal possible ?
        UserTaskAssociation associationTable = new UserTaskAssociation(connectedAppUser, task, true);

        taskRepository.save(task);
        userTaskAssociationRepository.save(associationTable);
        appUserRepository.save(connectedAppUser);

        return new TaskDto(task);
    }

    @Transactional
    public TaskDto updateTask(TaskDto taskDto) throws Exception {

        Optional<Task> taskToUpdate = taskRepository.findById(taskDto.getTaskId());

        if (taskToUpdate.isPresent()) {
            Task updateTaskTemp = taskToUpdate.get();
            updateTaskTemp.setTaskId(taskDto.getTaskId());
            updateTaskTemp.setTaskTitle(taskDto.getTaskTitle());
            updateTaskTemp.setDescription(taskDto.getDescription());
            updateTaskTemp.setLocation(taskDto.getLocation());
            updateTaskTemp.setStartingDate(taskDto.getStartingDate());
            updateTaskTemp.setStartingHour(taskDto.getStartingHour());
            updateTaskTemp.setEndingDate(taskDto.getEndingDate());
            updateTaskTemp.setEndingHour(taskDto.getEndingHour());

            Task task = taskRepository.save(updateTaskTemp);
            return new TaskDto(task);
        } else {
            throw new Exception();
        }
    }

    @Transactional
    public void shareTaskToUser(TaskDto taskDto, AppUserDto appUserDto) throws Exception {
        AppUser appUser = appUserRepository.findById(appUserDto.getAppUserId()).orElseThrow(() -> new Exception());
        Task task = taskRepository.findById(taskDto.getTaskId()).orElseThrow(() -> new Exception());

        UserTaskAssociation associationTable = new UserTaskAssociation(appUser, task, false);

        taskRepository.save(task);
        userTaskAssociationRepository.save(associationTable);
        appUserRepository.save(appUser);
    }

    @Transactional
    public void delete(Long id) throws Exception {
        Optional<Task> taskDelete = taskRepository.findById(id);
        userTaskAssociationRepository.deleteAllByTask(taskDelete.orElseThrow(() -> new Exception()));
        taskRepository.delete(taskDelete.orElseThrow(() -> new Exception()));//todo : ici créer une nouvelle exception de type task not found ?
    }

    @Transactional
    public void deleteUserAssociated(long taskID, AppUserDto connectedUser) throws Exception {
        Optional<Task> taskDelete = taskRepository.findById(taskID);
        userTaskAssociationRepository.deleteByTaskAndAppUser(taskDelete.orElseThrow(() -> new Exception()),
                appUserRepository.findById(connectedUser.getAppUserId()).orElseThrow(() -> new Exception()));
    }

    public boolean isSharedWithAppUser(TaskDto task, AppUserDto connectedUser) {
        return userTaskAssociationRepository.findAll().stream()
                .filter(userTaskAssociation -> Objects.equals(userTaskAssociation.getTask().getTaskId(), task.getTaskId()))
                .anyMatch(userTaskAssociation -> Objects.equals(userTaskAssociation.getUser().getAppUserId(), connectedUser.getAppUserId()));
    }

    public boolean isPropertyOfAppUser(TaskDto taskDto, AppUserDto connectedUser) throws Exception {
        if (!appUserRepository.findById(connectedUser.getAppUserId()).isPresent()) {
            System.out.println("AppUser not found in property check");
            return false;
        }
        if (!taskRepository.findById(taskDto.getTaskId()).isPresent()) {
            System.out.println("Task not found in property check");
            return false;
        }
        if (appUserRepository.findById(connectedUser.getAppUserId()).isPresent() && taskRepository.findById(taskDto.getTaskId()).isPresent()) {
            AppUser connectedAppUser = appUserRepository.findById(connectedUser.getAppUserId()).get();
            Task task = taskRepository.findById(taskDto.getTaskId()).get();
            return userTaskAssociationRepository
                    .findUserTaskAssociationByAppUserAndTask(connectedAppUser, task)
                    .isOwner();
        } else {
            System.out.println("AppUser not found in property check");
            return false;
        }

    }

    public List<String> getEmailsOfAppUsersWhoTaskIsSharedWith(TaskDto task) throws Exception {
        return taskRepository.findById(task.getTaskId()).orElseThrow(Exception::new).getAssociationList().stream()
                .map(asso -> asso.getUser().getEmail())
                .distinct()
                .collect(Collectors.toList());
    }
}
