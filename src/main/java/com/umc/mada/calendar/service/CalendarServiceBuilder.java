package com.umc.mada.calendar.service;

import com.umc.mada.calendar.repository.CalendarRepository;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;

public class CalendarServiceBuilder {
    private CalendarRepository calendarRepository;
    private UserRepository userRepository;

    public CalendarServiceBuilder setCalendarRepository(CalendarRepository calendarRepository,UserRepository userRepository) {
        this.userRepository = userRepository;
        this.calendarRepository = calendarRepository;
        return this;
    }

    public CalendarService createCalendarService() {
        return new CalendarService(calendarRepository,userRepository);
    }
}