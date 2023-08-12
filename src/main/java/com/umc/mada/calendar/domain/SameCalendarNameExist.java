package com.umc.mada.calendar.domain;

public class SameCalendarNameExist extends RuntimeException{
    public SameCalendarNameExist(String msg){
        super(msg);
    }
    public SameCalendarNameExist(){

    }
}
