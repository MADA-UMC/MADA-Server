package com.umc.mada.todo.domain;

import lombok.*;
import javax.persistence.*;
import com.umc.mada.global.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@Table(name = "TODO")
@NoArgsConstructor
@AllArgsConstructor
public class Todo extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 투두 ID (인조키)

    @Column(name = "user_id", nullable = false)
    private long userId; // 유저 ID

    //@ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "category_id", nullable = false)
    private int categoryId; // 카테고리 ID

    @Column(name = "todo_name", nullable = false, length = 100)
    private String todoName; // 투두 이름

    @Enumerated(EnumType.STRING)
    @Column(name = "repeat", nullable = false, length = 10)
    private Repeat repeat; // 반복 설정 (N: 반복 안함, day: 매일 반복, week: 매주 반복, month: 매월 반복)

    @Enumerated(EnumType.STRING)
    @Column(name = "repeat_week", length = 10)
    private RepeatWeek repeatWeek; // 매주 반복 요일 (null 또는 "mon", "tue", "wed", "thu", "fri", "sat", "sun")

    @Column(name = "complete", nullable = false)
    private boolean complete; // 완료 여부 (y: 오늘 할 일 완료, n: 오늘 할 일 미완료)

    @Column(name = "start_repeat_date")
    private LocalDate startRepeatDate; // 반복 시작 일자

    @Column(name = "end_repeat_date")
    private LocalDate endRepeatDate; // 반복 종료 일자

    @CreationTimestamp
    @Column(name="create_at",insertable=false, updatable=false)
    private Timestamp createAt;

    @UpdateTimestamp
    @Column(name = "update_at", insertable=false, updatable=false)
    private Timestamp updateAt;

    public enum Repeat {
        N,    // 반복 안함
        DAY,  // 매일 반복
        WEEK, // 매주 반복
        MONTH // 매월 반복
    }

    public enum RepeatWeek {
        MON, // 월요일
        TUE, // 화요일
        WED, // 수요일
        THU, // 목요일
        FRI, // 금요일
        SAT, // 토요일
        SUN  // 일요일
    }

    // 생성자 (필수 필드)
    public Todo(long userId, int categoryId, String todoName, boolean complete, Repeat repeat, RepeatWeek repeatWeek, LocalDate startRepeatDate, LocalDate endRepeatDate) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.todoName = todoName;
        this.complete = complete;
        this.repeat = repeat;
        this.repeatWeek = repeatWeek;
        this.startRepeatDate = startRepeatDate;
        this.endRepeatDate = endRepeatDate;
    }
}
