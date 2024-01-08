package com.umc.mada.calendar.repository;

import com.umc.mada.calendar.domain.Calendar;
import com.umc.mada.calendar.domain.RepeatCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface RepeatCalendarRepository extends JpaRepository<RepeatCalendar,Long > {
    List<RepeatCalendar> readRepeatCalendarsByCalendarIdAndIsExpiredIsFalse(Calendar calendarId);

}
