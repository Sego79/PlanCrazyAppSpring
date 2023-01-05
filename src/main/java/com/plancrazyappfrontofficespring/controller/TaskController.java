package com.plancrazyappfrontofficespring.controller;

import com.plancrazyappfrontofficespring.controller.dto.AppUserDto;
import com.plancrazyappfrontofficespring.controller.dto.TaskDto;
import com.plancrazyappfrontofficespring.model.AppUser;
import com.plancrazyappfrontofficespring.model.Task;
import com.plancrazyappfrontofficespring.security.jwt.JwtUtils;
import com.plancrazyappfrontofficespring.service.AppUserService;
import com.plancrazyappfrontofficespring.service.TaskService;
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

    @GetMapping("/task")
    public ResponseEntity<List<TaskDto>> getAppUserListTask(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth) throws Exception {
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        List<TaskDto> taskDtoList = taskService.fetchTaskOfUser(connectedUser);
        return ResponseEntity.status(HttpStatus.OK).body(taskDtoList);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<Task> getTaskById(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth, @PathVariable("id") long id) throws Exception {
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        // todo : rajouter une méthode qui vérifie que l'utilisateur connecté apparaît dans la liste d'utilisateurs
        Optional<Task> optTask = Optional.ofNullable(taskService.fetchById(id));
        if (optTask.isPresent()) {
            Task task = optTask.get();
            return new ResponseEntity<>(task, HttpStatus.OK);//todo : marche pas
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
//        if (optTask.isPresent()) {
//            Task task = optTask.get();
//            if(taskService.taskBelongsToUser(task, connectedUser)) {
//                System.out.println("utilisateur connecté");
//                return new ResponseEntity<>(task, HttpStatus.OK);
//            } else {
//                System.out.println("utilisateur non connecté");
//                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//            }
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
    }

    @PostMapping("/task")
    public ResponseEntity<TaskDto> createTask(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth, @RequestBody TaskDto dto) throws Exception {
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        TaskDto returnedTaskDto = taskService.addTask(dto, connectedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnedTaskDto);
    }

    @PutMapping("/task")
    public ResponseEntity<TaskDto> taskToEdit(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth, @RequestBody TaskDto taskDto) throws Exception {
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        // todo : rajouter une méthode qui vérifie que l'utilisateur connecté apparaît dans la liste d'utilisateurs
        TaskDto returnedTaskDto = taskService.updateTask(taskDto);
        return new ResponseEntity<>(returnedTaskDto, HttpStatus.OK);
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<HttpStatus> deleteAppUSer(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth, @PathVariable("id") long taskID) throws Exception {
        AppUserDto connectedUser = appUserService.getConnectedUser(headerAuth);
        // todo : rajouter une méthode qui vérifie que l'utilisateur connecté apparaît dans la liste d'utilisateurs
        try {
            taskService.delete(taskID);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
