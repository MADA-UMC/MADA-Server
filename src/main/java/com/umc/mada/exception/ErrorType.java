package com.umc.mada.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    BUY_OWNED_ITEM_ERROR(HttpStatus.CONFLICT, "이미 소유한 아이템입니다."),
    NOT_ALLOW_TO_WEARING(HttpStatus.BAD_REQUEST, "소유하지 않은 아이템입니다.");

    private final HttpStatus errorCode;
    private final String message;

}
