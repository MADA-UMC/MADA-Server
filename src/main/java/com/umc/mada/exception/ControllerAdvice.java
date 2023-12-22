package com.umc.mada.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(BuyOwnedItemException.class)
    public ResponseEntity<ErrorResponse> buyDuplicateItemHandler(final BuyOwnedItemException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NotAllowToWearingException.class)
    public ResponseEntity<ErrorResponse> notAllowToWearingHandler(final NotAllowToWearingException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }
    @ExceptionHandler(ServerInternalException.class)
    public ResponseEntity<ErrorResponse> testErrorHandler(final ServerInternalException e, HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Test Error")
                .url(httpServletRequest.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

//    @ExceptionHandler(Exception.class)
//    private ResponseEntity<ErrorResponse> unExpectedException(final Exception e){ //TODO: final HttpServletRequest request이거 추가해서 에러로그를 띄우기
//        return ResponseEntity.internalServerError().body(new ErrorResponse("예상치 못한 서버 에러가 발생했습니다."));
//    }
}
