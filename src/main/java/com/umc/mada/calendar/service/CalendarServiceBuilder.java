package com.umc.mada.calendar.service;

import com.umc.mada.calendar.repository.CalendarRepository;
import com.umc.mada.calendar.repository.RepeatCalendarRepository;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CalendarServiceBuilder {
    private CalendarRepository calendarRepository;
    private RepeatCalendarRepository repeatCalendarRepository;
    private UserRepository userRepository;
    @Bean
    public CalendarServiceBuilder setCalendarRepository(CalendarRepository calendarRepository,RepeatCalendarRepository repeatCalendarRepository ,UserRepository userRepository) {
        this.userRepository = userRepository;
        this.repeatCalendarRepository = repeatCalendarRepository;
        this.calendarRepository = calendarRepository;
        return this;
    }
    @Bean
    public CalendarService createCalendarService() {
        return new CalendarService(calendarRepository, repeatCalendarRepository,userRepository);
    }
}