package com.umc.mada.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;


public class ServerInternalException extends RuntimeException{
    private String msg;


    public ServerInternalException(String msg){
        super(msg);

    }


}
