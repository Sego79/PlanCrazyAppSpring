package com.plancrazyappfrontofficespring.service;

import com.plancrazyappfrontofficespring.controller.dto.AppUserDto;
import com.plancrazyappfrontofficespring.controller.dto.TaskDto;
import com.plancrazyappfrontofficespring.model.AppUser;
import com.plancrazyappfrontofficespring.model.Task;
import com.plancrazyappfrontofficespring.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<TaskDto> fetchTask() {
        List<Task> taskList = taskRepository.findAll();
        return taskList.stream()
                .map(g -> new TaskDto(g))
                .collect(Collectors.toList());
    }

    public Task fetchById(Long id) throws Exception {
        Optional<Task> taskOpt = taskRepository.findById(id);
        return taskOpt.orElseThrow(() -> new Exception());
    }

    @Transactional
    public TaskDto addTask(TaskDto dto){
        Task task =  new Task(
                dto.getTaskTitle(),
                dto.getDescription(),
                dto.getLocation(),
                dto.getStartingDate(),
                dto.getStartingHour(),
                dto.getEndingDate(),
                dto.getEndingHour(),
                dto.getPrivate()
        );

        taskRepository.save(task);
        return new TaskDto(task);
    }

    @Transactional
    public TaskDto updateTask(TaskDto taskDto) throws Exception {

        Optional<Task> taskToUpdate = taskRepository.findById(taskDto.getTaskId());

        if(taskToUpdate.isPresent()) {
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
    public void delete(Long id){
        taskRepository.deleteById(id);
    }
}
