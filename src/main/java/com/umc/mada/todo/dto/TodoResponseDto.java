package com.umc.mada.todo.dto;

import com.umc.mada.category.dto.CategoryResponseDto;
import com.umc.mada.todo.domain.Repeat;
import com.umc.mada.todo.domain.RepeatMonth;
import com.umc.mada.todo.domain.RepeatWeek;
import com.umc.mada.todo.domain.Todo;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
//@AllArgsConstructor
@NoArgsConstructor
public class TodoResponseDto {
    private int id;
    private LocalDate date; // 투두 일자
    private CategoryResponseDto category; // 카테고리 ID
    private String todoName; // 투두 이름
    private Boolean complete; // 완료 여부
    private Repeat repeat; // 반복 설정 (N: 반복 안함, day: 매일 반복, week: 매주 반복, month: 매월 반복)
    private RepeatWeek repeatWeek; // 매주 반복 요일 ("mon", "tue", "wed", "thu", "fri", "sat", "sun")
    private RepeatMonth repeatMonth;
    private LocalDate startRepeatDate; // 반복 시작일자
    private LocalDate endRepeatDate; // 반복 종료일자
    private Boolean isAlarm;
    private Boolean startTodoAtMonday;
    private Boolean endTodoBackSetting;
    private Boolean newTodoStartSetting;

    @Builder
    public TodoResponseDto(int id, LocalDate date, CategoryResponseDto category, String todoName, Boolean complete,
                           Repeat repeat, RepeatWeek repeatWeek, RepeatMonth repeatMonth, LocalDate startRepeatDate,
                           LocalDate endRepeatDate, Boolean isAlarm, Boolean startTodoAtMonday, Boolean endTodoBackSetting, Boolean newTodoStartSetting){
        this.id = id;
        this.date = date;
        this.category = category;
        this.todoName = todoName;
        this.complete = complete;
        this.repeat = repeat;
        this.repeatWeek = repeatWeek;
        this.repeatMonth = repeatMonth;
        this.startRepeatDate = startRepeatDate;
        this.endRepeatDate = endRepeatDate;
        this.isAlarm = isAlarm;
        this.startTodoAtMonday = startTodoAtMonday;
        this.endTodoBackSetting = endTodoBackSetting;
        this.newTodoStartSetting = newTodoStartSetting;
    }
    public static TodoResponseDto of(Todo todo){
        return TodoResponseDto.builder()
                .id(todo.getId())
                .date(todo.getDate())
                .category(CategoryResponseDto.of(todo.getCategory()))
                .todoName(todo.getTodoName())
                .complete(todo.getComplete())
                .repeat(todo.getRepeat())
                .repeatWeek(todo.getRepeatWeek())
                .repeatMonth(todo.getRepeatMonth())
                .startRepeatDate(todo.getStartRepeatDate())
                .endRepeatDate(todo.getEndRepeatDate())
                .isAlarm(todo.getUserId().isAlarm())
                .startTodoAtMonday(todo.getUserId().isStartTodoAtMonday())
                .endTodoBackSetting(todo.getUserId().isEndTodoBackSetting())
                .newTodoStartSetting(todo.getUserId().isNewTodoStartSetting())
                .build();
    }
}
