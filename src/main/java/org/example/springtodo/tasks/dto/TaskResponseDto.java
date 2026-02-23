package org.example.springtodo.tasks.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.springtodo.tasks.domain.TaskStatus;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate deadline;
    private TaskStatus taskStatus;
}
