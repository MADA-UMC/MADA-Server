package com.umc.mada.timetable.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CommentRequestDto {
    private LocalDate date;
    private String content;
}
