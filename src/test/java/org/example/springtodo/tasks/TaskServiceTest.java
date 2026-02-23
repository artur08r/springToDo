package org.example.springtodo.tasks;

import org.example.springtodo.tasks.bd.Task;
import org.example.springtodo.tasks.bd.TaskRepository;
import org.example.springtodo.tasks.domain.TaskService;
import org.example.springtodo.tasks.domain.TaskStatus;
import org.example.springtodo.tasks.dto.TaskRequestDto;
import org.example.springtodo.tasks.dto.TaskResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    @Test
    void create_shouldSaveTaskWithTodoStatus() {

        TaskRequestDto dto = new TaskRequestDto();
        dto.setTitle("Test task");
        dto.setDescription("Test desc");
        dto.setDeadline(LocalDate.now());

        Task saved = Task.builder()
                .id(1L)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .deadline(dto.getDeadline())
                .status(TaskStatus.TODO)
                .build();

        when(repository.save(any(Task.class))).thenReturn(saved);

        TaskResponseDto response = service.create(dto);

        assertNotNull(response);
        assertEquals("Test task", response.getTitle());
        assertEquals(TaskStatus.TODO, response.getTaskStatus());

        verify(repository).save(any(Task.class));
    }

    @Test
    void update_shouldModifyExistingTask() {

        Task existing = Task.builder()
                .id(1L)
                .title("old")
                .description("old")
                .status(TaskStatus.TODO)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        TaskRequestDto dto = new TaskRequestDto();
        dto.setTitle("new");
        dto.setDescription("new");
        dto.setTaskStatus(TaskStatus.DONE);

        TaskResponseDto result = service.update(1L, dto);

        assertEquals("new", existing.getTitle());
        assertEquals("new", existing.getDescription());
        assertEquals(TaskStatus.DONE, existing.getStatus());

        verify(repository).findById(1L);
        verify(repository, never()).save(any());
    }

    @Test
    void delete_shouldCallRepositoryDelete() {

        Long id = 1L;

        when(repository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void getAll_shouldFilterByStatus() {

        Task doneTask = Task.builder()
                .id(1L)
                .status(TaskStatus.DONE)
                .build();

        when(repository.findByStatus(TaskStatus.DONE))
                .thenReturn(List.of(doneTask));

        List<TaskResponseDto> result = service.getAll(TaskStatus.DONE, null);

        assertEquals(1, result.size());
        assertEquals(TaskStatus.DONE, result.get(0).getTaskStatus());

        verify(repository).findByStatus(TaskStatus.DONE);
        verify(repository, never()).findAll();
    }

    @Test
    void getAll_shouldReturnAllTasksWhenStatusNull() {

        when(repository.findAll()).thenReturn(List.of(new Task(), new Task()));

        List<TaskResponseDto> result = service.getAll(null, null);

        assertEquals(2, result.size());

        verify(repository).findAll();
    }

    @Test
    void getAll_shouldSortByDeadline() {

        Task late = Task.builder()
                .id(1L)
                .deadline(LocalDate.now().plusDays(10))
                .build();

        Task early = Task.builder()
                .id(2L)
                .deadline(LocalDate.now().plusDays(1))
                .build();

        when(repository.findAll()).thenReturn(new ArrayList<>(List.of(early, late)));

        List<TaskResponseDto> result = service.getAll(null, "deadline");

        assertEquals(2L, result.get(0).getId()); // ранняя дата должна быть первой
        assertEquals(1L, result.get(1).getId());
    }

}
