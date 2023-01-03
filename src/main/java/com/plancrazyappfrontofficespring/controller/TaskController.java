package com.plancrazyappfrontofficespring.controller;

import com.plancrazyappfrontofficespring.controller.dto.TaskDto;
import com.plancrazyappfrontofficespring.model.Task;
import com.plancrazyappfrontofficespring.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/task")
    public ResponseEntity<List<TaskDto>> getAllTask() {
        List<TaskDto> taskDtoList = taskService.fetchTask();
        return ResponseEntity.status(HttpStatus.OK).body(taskDtoList);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") long id) throws Exception {
        Optional<Task> optTask = Optional.ofNullable(taskService.fetchById(id));
        if (optTask.isPresent()) {
            return new ResponseEntity<>(optTask.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/task")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto dto) {
        TaskDto returnedTaskDto = taskService.addTask(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnedTaskDto);
    }

    @PutMapping("/task")
    public ResponseEntity<TaskDto> taskToEdit(@RequestBody TaskDto taskDto) throws Exception {

        TaskDto returnedTaskDto = taskService.updateTask(taskDto);
        return new ResponseEntity<>(returnedTaskDto, HttpStatus.OK);
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<HttpStatus> deleteAppUSer(@PathVariable("id") long taskID){
        try {
            taskService.delete(taskID);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
