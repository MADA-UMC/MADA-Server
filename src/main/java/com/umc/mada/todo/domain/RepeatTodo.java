package com.umc.mada.todo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "REPEAT_TODO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@AllArgsConstructor
@NoArgsConstructor
public class RepeatTodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name="todo_id")
    @ManyToOne
    private Todo todoId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "complete", nullable = false)
    private Boolean complete;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    public RepeatTodo(Todo todoId, LocalDate date, Boolean complete, Boolean isDeleted) {
        this.todoId = todoId;
        this.date = date;
        this.complete = complete;
        this.isDeleted = isDeleted;
    }
}
