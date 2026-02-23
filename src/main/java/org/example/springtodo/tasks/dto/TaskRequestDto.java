package org.example.springtodo.tasks.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.springtodo.tasks.domain.TaskStatus;

import java.time.LocalDate;

@Getter
@Setter
public class TaskRequestDto {
    private String title;
    private String description;
    private LocalDate deadline;
    private TaskStatus taskStatus;
}
