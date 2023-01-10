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
            if(taskService.isSharedWithAppUser(task, connectedUser)) {
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
        System.out.println(dto);
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        System.out.println(connectedUser);
        TaskDto returnedTaskDto = taskService.addTask(dto, connectedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnedTaskDto);
    }

    @PutMapping("/task")
    public ResponseEntity<TaskDto> editTask(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth, @RequestBody TaskDto taskDto) throws Exception {
        System.out.println(taskDto);
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        System.out.println(connectedUser);
        if (taskService.isSharedWithAppUser(taskDto, connectedUser)) {
            TaskDto returnedTaskDto = taskService.updateTask(taskDto);
            return new ResponseEntity<>(returnedTaskDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<HttpStatus> deleteTask(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth, @PathVariable("id") long taskID) throws Exception {
        System.out.println(taskID);
        TaskDto taskDto = taskService.fetchById(taskID);
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        System.out.println(connectedUser);
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

        System.out.println("\nTache à partager :");
        TaskDto taskDto = taskService.fetchById(shareRequestBody.getTaskId());
        System.out.println(taskDto);

        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        System.out.println("Utilisateur connecté :");
        System.out.println(connectedUser);

        System.out.println("Mail reçu :");
        System.out.println(shareRequestBody.getAppUserToShareEmail());

        if (taskService.isSharedWithAppUser(taskDto, connectedUser)) {
            try {
                if (appUserService.existsByEmail(shareRequestBody.getAppUserToShareEmail())) {
                    AppUser appUserToShare = appUserService.fetchByEmail(shareRequestBody.getAppUserToShareEmail());
                    System.out.println("Utilisateur à qui partager :");
                    System.out.println(appUserToShare);

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
            if(taskService.isSharedWithAppUser(task, connectedUser)) {
                System.out.printf("\nOn veut la liste des utilisateurs qui ont eu les droits sur la tâche n°%d.\n", id);
                List<String> appUsersWhoTaskIsSharedWith = taskService.getEmailsOfAppUsersWhoTaskIsSharedWith(task);
                return new ResponseEntity<>(appUsersWhoTaskIsSharedWith, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else { //todo : gérer cas id not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/task/share") // todo
    public ResponseEntity<HttpStatus> unshareThisTaskWithAppUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth,
                                                                 @RequestBody ShareRequest shareRequestBody) throws Exception {
        System.out.printf("\nOn veut supprimer les droits d'accès éventuels de un utilisateur donné de la tâche %d.\n",
                shareRequestBody.getTaskId());

        System.out.println("L'utilisateur connecté est :");
        TaskDto taskDto = taskService.fetchById(shareRequestBody.getTaskId());
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        System.out.println(connectedUser);

        if (taskService.isPropertyOfAppUser(taskDto, connectedUser)) {
            try {
                System.out.println("IS OK. Maintenant on veut : l'association précise entre les 2 éléments de la shareRequest.");
                AppUser appUserToUnshare = appUserService.fetchByEmail(shareRequestBody.getAppUserToShareEmail());
                System.out.println("Utilisateur à qui départager :");
                System.out.println(appUserToUnshare);
                userTaskAssociationService.delete(appUserToUnshare, taskDto);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/task/share/allUsers") // todo
    public ResponseEntity<HttpStatus> unshareTaskWithAllAppUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth,
                                                                 @RequestBody ShareRequest shareRequestBody) throws Exception {
        System.out.printf("\nOn veut supprimer les droits d'accès éventuels de tous les utilisateurs la tâche %d.\n",
                shareRequestBody.getTaskId());

        System.out.println("L'utilisateur connecté est :");
        TaskDto taskDto = taskService.fetchById(shareRequestBody.getTaskId());
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        System.out.println(connectedUser);


        if (taskService.isPropertyOfAppUser(taskDto, connectedUser)) {
            try {
                System.out.println("IS OK. Maintenant on veut : supprimer toutes les associations, puis recréer l'association.");
                userTaskAssociationService.deleteAllNonOwnerUserOfThisTask(taskDto);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/task/share/allTasks") // todo
    public ResponseEntity<HttpStatus> unshareAllTasksWithAppUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth,
                                                                 @RequestBody ShareRequest shareRequestBody) throws Exception {
        System.out.println("\nOn veut supprimer les droits d'accès éventuels d'un utilisateur à toutes les tâches de l'utilisateur connecté.");

        System.out.println("L'utilisateur connecté est :");
        TaskDto taskDto = taskService.fetchById(shareRequestBody.getTaskId());
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        System.out.println(connectedUser);

        if (taskService.isPropertyOfAppUser(taskDto, connectedUser)) {
            try {
                System.out.println("IS OK. Maintenant on veut : trouver toutes les associations de l'utilisateur connecté avec ");
                AppUser appUserToUnshare = appUserService.fetchByEmail(shareRequestBody.getAppUserToShareEmail());
                System.out.println("Utilisateur à qui départager :");
                System.out.println(appUserToUnshare);
                userTaskAssociationService.deleteAllOwnedByUser1TasksOfUser2(connectedUser, appUserToUnshare);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/task/share/allTasks") // todo
    public ResponseEntity<HttpStatus> shareAllTasksWithAppUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth,
                                                                 @RequestBody ShareRequest shareRequestBody) throws Exception {
        System.out.println("\nOn veut autoriser l'accès d'un utilisateur à toutes les tâches.");

        System.out.println("L'utilisateur connecté est :");
        TaskDto taskDto = taskService.fetchById(shareRequestBody.getTaskId());
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        System.out.println(connectedUser);

        if (taskService.isPropertyOfAppUser(taskDto, connectedUser)) {
            try {
                System.out.println("IS OK. Maintenant on veut : créer les associations de toutes les tâches de l'utilisateur connecté avec ");
                AppUser appUserToShare = appUserService.fetchByEmail(shareRequestBody.getAppUserToShareEmail());
                System.out.println("Utilisateur à qui partager :");
                System.out.println(appUserToShare);
                userTaskAssociationService.associateAllTasksOwnedByUser1ToUser2(connectedUser, appUserToShare);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
