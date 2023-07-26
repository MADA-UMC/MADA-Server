package com.umc.mada.todo.domain;

import lombok.*;
import javax.persistence.*;
import com.umc.mada.global.BaseEntity;
import java.time.LocalDate;
import java.util.List;
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
    private int id; // Todo ID (인조키)

    @Column(name = "user_id", nullable = false)
    private long user_id; // 유저 ID

    @Column(name = "category_id", nullable = false)
    private int category_id; // 카테고리 ID

    @Column(name = "todo_name", nullable = false, length = 100)
    private String todo_name; // Todo 이름

    @Column(name = "complete", nullable = false, length = 1)
    private char complete; // 완료 여부 (y: 오늘 할 일 완료, n: 오늘 할 일 미완료)

    @Column(name = "startRepeatDate")
    private LocalDate startRepeatDate; // 반복 시작 일자

    @Column(name = "endRepeatDate")
    private LocalDate endRepeatDate; // 반복 종료 일자

    @Column(name = "isRepeatMon")
    private boolean isRepeatMon; // 월요일 반복 여부

    @Column(name = "isRepeatTue")
    private boolean isRepeatTue; // 화요일 반복 여부

    @Column(name = "isRepeatWed")
    private boolean isRepeatWed; // 수요일 반복 여부

    @Column(name = "isRepeatThu")
    private boolean isRepeatThu; // 목요일 반복 여부

    @Column(name = "isRepeatFri")
    private boolean isRepeatFri; // 금요일 반복 여부

    @Column(name = "isRepeatSat")
    private boolean isRepeatSat; // 토요일 반복 여부

    @Column(name = "isRepeatSun")
    private boolean isRepeatSun; // 일요일 반복 여부

    //@ElementCollection
    //@CollectionTable(name = "TODO_REPEAT_DATES", joinColumns = @JoinColumn(name = "todo_id"))
    @Column(name = "monthly_repeat_date")
    private List<Integer> monthlyRepeatDates; // 매월 반복하는 날짜 목록 (예: [5, 15])

    // 생성자 (필수 필드)
    public Todo(long user_id, int category_id, String todo_name, char complete) {
        this.user_id = user_id;
        this.category_id = category_id;
        this.todo_name = todo_name;
        this.complete = complete;
    }
}
