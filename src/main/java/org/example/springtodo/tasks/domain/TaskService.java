package org.example.springtodo.tasks.domain;

import jakarta.transaction.Transactional;
import org.example.springtodo.tasks.bd.Task;
import org.example.springtodo.tasks.bd.TaskRepository;
import org.example.springtodo.tasks.dto.TaskRequestDto;
import org.example.springtodo.tasks.dto.TaskResponseDto;
import org.example.springtodo.tasks.exception.TaskNotFoundException;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;


@Service
@Transactional
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository){
        this.repository = repository;
    }


    public TaskResponseDto create(TaskRequestDto dto) {
        Task newTask = Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .deadline(dto.getDeadline())
                .status(TaskStatus.TODO)
                .build();

        return mapToDto(repository.save(newTask));
    }

    public TaskResponseDto update(Long id, TaskRequestDto dto) {
        Task task = repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDeadline(dto.getDeadline());
        task.setStatus(dto.getTaskStatus());

        return mapToDto(task);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }

        repository.deleteById(id);
    }

    public List<TaskResponseDto> getAll(TaskStatus taskStatus, String sortBy) {
        List<Task> tasks;

        if (taskStatus != null){
            tasks = repository.findByStatus(taskStatus);
        }else{
            tasks = repository.findAll();
        }

        if("deadline".equalsIgnoreCase(sortBy)){
            tasks.sort(Comparator.comparing(Task::getDeadline));
        }

        if("taskStatus".equalsIgnoreCase(sortBy)){
            tasks.sort(Comparator.comparing(Task::getStatus));
        }

        return tasks.stream()
                .map(this::mapToDto)
                .toList();
    }

    private TaskResponseDto mapToDto(Task task){
        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .taskStatus(task.getStatus())
                .build();

    }
}
