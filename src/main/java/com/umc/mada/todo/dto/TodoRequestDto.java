package com.umc.mada.todo.dto;

import com.umc.mada.category.domain.Category;
import lombok.*;
import com.umc.mada.todo.domain.Repeat;
import reactor.util.annotation.Nullable;

import java.time.LocalDate;

@Data
public class TodoRequestDto {
    private LocalDate date; // 투두 일자
    private Category category; // 카테고리 ID
    private String todoName; // 투두 이름
    private Boolean complete; // 완료 여부 (y: 오늘 할 일 완료, n: 오늘 할 일 미완료)
    private Repeat  repeat; // 반복 설정 (N: 반복 안함, day: 매일 반복, week: 매주 반복, month: 매월 반복)
    private Integer repeatInfo;
    private LocalDate startRepeatDate; // 반복 시작 일자
    private LocalDate endRepeatDate; // 반복 종료 일자
    private Boolean isDeleted; // 투두 삭제 여부
}
