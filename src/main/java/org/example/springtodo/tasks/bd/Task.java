package org.example.springtodo.tasks.bd;

import jakarta.persistence.*;
import lombok.*;
import org.example.springtodo.tasks.domain.TaskStatus;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Column(name = "task_status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
}
