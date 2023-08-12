package com.umc.mada.todo.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Repeat {
    N,
    DAY,
    WEEK,
    MONTH
}
