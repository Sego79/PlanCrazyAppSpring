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

    public List<TaskDto> fetchTaskOfUser(AppUserDto connectedUser) {
        return userTaskAssociationRepository.findAll().stream()
                .filter(userTaskAssociation -> userTaskAssociation.getUser().getAppUserId() == connectedUser.getAppUserId())
                .map(userTaskAssociation -> {
                    try {
                        return taskDtoWithOwnerNickname(userTaskAssociation);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private static TaskDto taskDtoWithOwnerNickname(UserTaskAssociation userTaskAssociation) throws Exception {
        TaskDto taskDto = new TaskDto(userTaskAssociation.getTask());
        if (userTaskAssociation.isOwner()){
            taskDto.setOwnerNickname(userTaskAssociation.getUser().getNickname());
        } else {
            String ownerNickname = userTaskAssociation.getTask().getAssociationList().stream()
                    .filter(asso -> asso.isOwner())
                    .map(asso -> asso.getUser().getNickname())
                    .findFirst()
                    .orElseThrow(() -> new Exception());
            taskDto.setOwnerNickname(ownerNickname);
        }
        return taskDto;
    }

    public TaskDto fetchById(Long id) throws Exception {
        Optional<Task> taskOpt = taskRepository.findById(id);
        return new TaskDto(taskOpt.orElseThrow(() -> new Exception()));
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
        System.out.println("Id reçu par la fonction delete de TaskService : " + id);
        Optional<Task> taskDelete = taskRepository.findById(id);
        userTaskAssociationRepository.deleteAllByTask(taskDelete.orElseThrow(() -> new Exception()));
        taskRepository.delete(taskDelete.orElseThrow(() -> new Exception()));//todo : ici créer une nouvelle exception de type task not found ?
    }

//    @Transactional
//    public void

    public boolean taskBelongsToUser(TaskDto task, AppUserDto connectedUser) {
        return userTaskAssociationRepository.findAll().stream()
                .filter(userTaskAssociation -> userTaskAssociation.getTask().getTaskId() == task.getTaskId())
                .anyMatch(userTaskAssociation -> userTaskAssociation.getUser().getAppUserId() == connectedUser.getAppUserId());
    }
}
