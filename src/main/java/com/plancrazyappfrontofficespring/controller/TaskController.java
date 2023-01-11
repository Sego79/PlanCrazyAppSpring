package com.plancrazyappfrontofficespring.controller;

import com.plancrazyappfrontofficespring.controller.dto.AppUserDto;
import com.plancrazyappfrontofficespring.controller.dto.ShareRequest;
import com.plancrazyappfrontofficespring.controller.dto.TaskDto;
import com.plancrazyappfrontofficespring.model.AppUser;
import com.plancrazyappfrontofficespring.service.AppUserService;
import com.plancrazyappfrontofficespring.service.TaskService;
import com.plancrazyappfrontofficespring.service.UserTaskAssociationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private UserTaskAssociationService userTaskAssociationService;

    @GetMapping("/task")
    public ResponseEntity<List<TaskDto>> getConnectedAppUserListTask(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth) throws Exception {
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        List<TaskDto> taskDtoList = taskService.fetchTaskOfUser(connectedUser);
        return ResponseEntity.status(HttpStatus.OK).body(taskDtoList);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDto> getTaskById(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth, @PathVariable("id") long id) throws Exception {
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        Optional<TaskDto> optTask = Optional.ofNullable(taskService.fetchById(id));
        if (optTask.isPresent()) {
            TaskDto task = optTask.get();
            if (taskService.isSharedWithAppUser(task, connectedUser)) {
                return new ResponseEntity<>(task, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else { //todo : gérer cas id not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/task")
    public ResponseEntity<TaskDto> createTask(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth, @RequestBody TaskDto dto) throws Exception {
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        TaskDto returnedTaskDto = taskService.addTask(dto, connectedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnedTaskDto);
    }

    @PutMapping("/task")
    public ResponseEntity<TaskDto> editTask(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth, @RequestBody TaskDto taskDto) throws Exception {
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        if (taskService.isSharedWithAppUser(taskDto, connectedUser)) {
            TaskDto returnedTaskDto = taskService.updateTask(taskDto);
            return new ResponseEntity<>(returnedTaskDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<HttpStatus> deleteTask(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth, @PathVariable("id") long taskID) throws Exception {
        TaskDto taskDto = taskService.fetchById(taskID);
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        if (taskService.isSharedWithAppUser(taskDto, connectedUser)) {
            try {
                if (taskDto.getOwnerEmail().equals(connectedUser.getEmail())) {
                    taskService.delete(taskID);
                } else {
                    taskService.deleteUserAssociated(taskID, connectedUser);
                }
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/task/share")
    public ResponseEntity<TaskDto> shareTask(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth,
                                             @RequestBody ShareRequest shareRequestBody) throws Exception {
        TaskDto taskDto = taskService.fetchById(shareRequestBody.getTaskId());
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        if (taskService.isPropertyOfAppUser(taskDto, connectedUser)) {
            try {
                if (appUserService.existsByEmail(shareRequestBody.getAppUserToShareEmail())) {
                    AppUser appUserToShare = appUserService.fetchByEmail(shareRequestBody.getAppUserToShareEmail());
                    taskService.shareTaskToUser(taskDto, new AppUserDto(appUserToShare));
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/task/share/{id}")
    public ResponseEntity<List<String>> getSharedUsersEmailsOfTaskByTaskId(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth,
                                                                           @PathVariable("id") long id) throws Exception {
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        Optional<TaskDto> optTask = Optional.ofNullable(taskService.fetchById(id));
        if (optTask.isPresent()) {
            TaskDto task = optTask.get();
            if (taskService.isSharedWithAppUser(task, connectedUser)) {
                List<String> appUsersWhoTaskIsSharedWith = taskService.getEmailsOfAppUsersWhoTaskIsSharedWith(task);
                return new ResponseEntity<>(appUsersWhoTaskIsSharedWith, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else { //todo : gérer cas id not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/task/share")
    public ResponseEntity<HttpStatus> unshareThisTaskWithAppUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth,
                                                                 @RequestBody ShareRequest shareRequestBody) throws Exception {
        TaskDto taskDto = taskService.fetchById(shareRequestBody.getTaskId());
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        if (taskService.isPropertyOfAppUser(taskDto, connectedUser)) {
            try {
                AppUser appUserToUnshare = appUserService.fetchByEmail(shareRequestBody.getAppUserToShareEmail());
                userTaskAssociationService.delete(appUserToUnshare, taskDto);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/task/share/allUsers")
    public ResponseEntity<HttpStatus> unshareTaskWithAllAppUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth,
                                                                 @RequestBody ShareRequest shareRequestBody) throws Exception {
        TaskDto taskDto = taskService.fetchById(shareRequestBody.getTaskId());
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        if (taskService.isPropertyOfAppUser(taskDto, connectedUser)) {
            try {
                userTaskAssociationService.deleteAllNonOwnerUserOfThisTask(taskDto);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/task/share/allTasks")
    public ResponseEntity<HttpStatus> unshareAllTasksWithAppUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth,
                                                                 @RequestBody ShareRequest shareRequestBody) throws Exception {
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        try {
            AppUser appUserToUnshare = appUserService.fetchByEmail(shareRequestBody.getAppUserToShareEmail());
            userTaskAssociationService.deleteAllOwnedByUser1TasksOfUser2(connectedUser, appUserToUnshare);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/task/share/allTasks")
    public ResponseEntity<HttpStatus> shareAllTasksWithAppUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth,
                                                               @RequestBody ShareRequest shareRequestBody) throws Exception {
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        try {
            AppUser appUserToShare = appUserService.fetchByEmail(shareRequestBody.getAppUserToShareEmail());
            userTaskAssociationService.associateAllTasksOwnedByUser1ToUser2(connectedUser, appUserToShare);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
