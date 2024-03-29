package com.umc.mada.todo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.umc.mada.category.domain.Category;
import com.umc.mada.user.domain.User;
import lombok.*;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "TODO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@AllArgsConstructor
@NoArgsConstructor
public class Todo{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 투두 ID (인조키)

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User userId; // 유저 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category; // 카테고리 ID

    @Column(name = "todo_name", nullable = false)
    private String todoName; // 투두 이름

    @Column(name = "`repeat`", nullable = false, length = 10)
    @Enumerated(value = EnumType.STRING)
    private Repeat repeat; // 반복 설정 (N: 반복 안함, day: 매일 반복, week: 매주 반복, month: 매월 반복)

    @Column(name = "repeat_info")
    private Integer repeatInfo; // 반복 정보

    @Column(name = "complete", nullable = false)
    private Boolean complete; // 완료 여부 (y: 오늘 할 일 완료, n: 오늘 할 일 미완료)

    @Column(name = "start_repeat_date")
    private LocalDate startRepeatDate; // 반복 시작 일자

    @Column(name = "end_repeat_date")
    private LocalDate endRepeatDate; // 반복 종료 일자

    @Column(name = "date", nullable = false)
    private LocalDate date; // 투두 일자

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted; // 투두 삭제 여부

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt; // 생성 시간

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt; // 수정 시간

    // 생성자 (필수 필드)
    public Todo(User userId, LocalDate date, Category category, String todoName, boolean complete, Repeat repeat, Integer repeatInfo, LocalDate startRepeatDate, LocalDate endRepeatDate, Boolean isDeleted) {
        this.userId = userId;
        this.date = date;
        this.category = category;
        this.todoName = todoName;
        this.complete = complete;
        this.repeat = repeat;
        this.repeatInfo = repeatInfo;
        this.startRepeatDate = startRepeatDate;
        this.endRepeatDate = endRepeatDate;
        this.isDeleted = isDeleted;
    }
}
