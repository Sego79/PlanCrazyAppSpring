package com.plancrazyappfrontofficespring.controller;

import com.plancrazyappfrontofficespring.controller.dto.AppUserDto;
import com.plancrazyappfrontofficespring.controller.dto.TaskDto;
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
    private JwtUtils jwtUtils;
    @Autowired
    private AppUserService appUserService;

    @GetMapping("/task")
    public ResponseEntity<List<TaskDto>> getAppUserListTaskTask() { //todo : il faudrait pour récupérer ici uniquement les tâches de l'utilisateur connecté
        List<TaskDto> taskDtoList = taskService.fetchTask();
        return ResponseEntity.status(HttpStatus.OK).body(taskDtoList);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") long id) throws Exception {
        Optional<Task> optTask = Optional.ofNullable(taskService.fetchById(id)); // todo : rajouter une méthode qui vérifie que l'utilisateur connecté apparaît dans la liste d'utilisateurs
        if (optTask.isPresent()) {
            return new ResponseEntity<>(optTask.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/task")
    public ResponseEntity<TaskDto> createTask(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuth, @RequestBody TaskDto dto) throws Exception {
        String email = jwtUtils.getEmailFromToken(jwtUtils.parseStringHeaderAuthorization(headerAuth));
        AppUserDto connectedUser = new AppUserDto(appUserService.fetchByEmail(email));
        TaskDto returnedTaskDto = taskService.addTask(dto, connectedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnedTaskDto);
    }

    @PutMapping("/task")
    public ResponseEntity<TaskDto> taskToEdit(@RequestBody TaskDto taskDto) throws Exception {
// todo : rajouter une méthode qui vérifie que l'utilisateur connecté apparaît dans la liste d'utilisateurs
        TaskDto returnedTaskDto = taskService.updateTask(taskDto);
        return new ResponseEntity<>(returnedTaskDto, HttpStatus.OK);
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<HttpStatus> deleteAppUSer(@PathVariable("id") long taskID){
        // todo : rajouter une méthode qui vérifie que l'utilisateur connecté apparaît dans la liste d'utilisateurs
        try {
            taskService.delete(taskID);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
