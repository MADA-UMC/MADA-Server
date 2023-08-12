package com.umc.mada.todo.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum RepeatWeek {
    MON,
    TUE,
    WED,
    THU,
    FRI,
    SAT,
    SUN;
}
