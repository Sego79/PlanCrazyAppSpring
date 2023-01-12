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
import java.util.stream.Collectors;

@Service
public class UserTaskAssociationService {

    @Autowired
    private UserTaskAssociationRepository userTaskAssociationRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserService appUserService;
    @Autowired
    private TaskService taskService;


    @Transactional
    public void delete(AppUser appUserToUnshare, TaskDto taskDto) throws Exception {
        Task taskToDelete = taskRepository.findById(taskDto.getTaskId()).orElseThrow(() -> new Exception());
        userTaskAssociationRepository.deleteByTaskAndAppUser(
                taskToDelete,
                appUserToUnshare
        );
    }

    @Transactional
    public void deleteAllNonOwnerUserOfThisTask(TaskDto taskDto) throws Exception {
        Task task = taskRepository.findById(taskDto.getTaskId()).orElseThrow(() -> new Exception());
        task.getAssociationList().stream()
                .filter(userTaskAssociation -> !userTaskAssociation.isOwner())
                .forEach(userTaskAssociation -> userTaskAssociationRepository.delete(userTaskAssociation));
    }

    @Transactional
    public void deleteAllOwnedByUser1TasksOfUser2(AppUserDto appUser1Dto, AppUser appUser2) throws Exception {
        AppUser appUser1 = appUserRepository.findUserByEmail(appUser1Dto.getEmail()).orElseThrow(() -> new Exception());
        appUser2.getUserTaskAssociationList().stream()
                .filter(userTaskAssociation -> {
                    try {
                        return appUser1.getEmail() == userTaskAssociation.getTask().getAssociationList().stream()
                                .filter(asso -> asso.isOwner())
                                .map(asso -> asso.getUser().getEmail())
                                .findFirst().orElseThrow(() -> new Exception());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .forEach(userTaskAssociation -> userTaskAssociationRepository.delete(userTaskAssociation));
    }

    @Transactional
    public void associateAllTasksOwnedByUser1ToUser2(AppUserDto appUser1Dto, AppUser appUser2) throws Exception {
        AppUser appUser1 = appUserRepository.findUserByEmail(appUser1Dto.getEmail()).orElseThrow(() -> new Exception());
        appUser1.getUserTaskAssociationList().stream()
                .filter(userTaskAssociation -> userTaskAssociation.isOwner())
                .map(asso -> asso.getTask())
                .forEach(task -> {
                    try {
                        taskService.shareTaskToUser(new TaskDto(task), new AppUserDto(appUser2));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public List<String> findAllUsersEmailsThisUserHadSharedTasksWith(AppUserDto appUserDto) throws Exception {
        AppUser appUser = appUserRepository.findUserByEmail(appUserDto.getEmail()).orElseThrow(() -> new Exception());
        List<Task> tasksPropertyOfAppUser = appUser.getUserTaskAssociationList().stream()
                .filter(userTaskAssociation -> userTaskAssociation.isOwner())
                .map(asso -> asso.getTask())
                .collect(Collectors.toList());
        List<UserTaskAssociation> associationBetweenTasksPropertyOfAppUserAndOtherUsers = tasksPropertyOfAppUser.stream()
                .flatMap(task -> task.getAssociationList().stream())
                .filter(asso -> !asso.isOwner())
                .collect(Collectors.toList());
        return associationBetweenTasksPropertyOfAppUserAndOtherUsers.stream()
                .map(asso -> asso.getUser().getEmail())
                .distinct()
                .collect(Collectors.toList());
    }
}
