package com.plancrazyappfrontofficespring.service;

import com.plancrazyappfrontofficespring.controller.dto.TaskDto;
import com.plancrazyappfrontofficespring.model.Task;
import com.plancrazyappfrontofficespring.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
