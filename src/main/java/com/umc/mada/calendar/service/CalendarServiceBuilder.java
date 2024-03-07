package com.umc.mada.calendar.service;

import com.umc.mada.calendar.repository.CalendarRepository;
import com.umc.mada.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CalendarServiceBuilder {
    private CalendarRepository calendarRepository;

    private UserRepository userRepository;
    @Bean
    public CalendarServiceBuilder setCalendarRepository(CalendarRepository calendarRepository ,UserRepository userRepository) {
        this.userRepository = userRepository;

        this.calendarRepository = calendarRepository;
        return this;
    }
    @Bean
    public CalendarService createCalendarService() {
        return new CalendarService(calendarRepository,userRepository);
    }
}