package org.example.springtodo.tasks.api;

import org.example.springtodo.tasks.bd.Task;
import org.example.springtodo.tasks.domain.TaskService;
import org.example.springtodo.tasks.domain.TaskStatus;
import org.example.springtodo.tasks.dto.TaskRequestDto;
import org.example.springtodo.tasks.dto.TaskResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service){
        this.service = service;
    }

    @PostMapping
    public TaskResponseDto create(@RequestBody TaskRequestDto dto){
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public TaskResponseDto update(@PathVariable Long id, @RequestBody TaskRequestDto dto){
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
         service.delete(id);
    }

    @GetMapping
    public List<TaskResponseDto> getAll(
            @RequestParam(required = false) TaskStatus taskStatus,
            @RequestParam(required = false) String sortBy
    ){
        return service.getAll(taskStatus, sortBy);
    }


}
