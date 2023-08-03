package com.umc.mada.calendar.repository;

import com.umc.mada.calendar.domain.Calendar;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar,Long> {
    //Optional<Calendar> findCalendarById(Long id); 캘린더 고유 아이디로 캘린더 조회
    List<Calendar> findAllByUid(Long uid);
    Calendar findCalendarById(Long id);
    boolean deleteCalendarById(Long id);
    boolean existsCalendarByUidAndEndDateBetweenAndCalenderName(Long uid, Date startDate, Date endDate, String calendarName);
    @Override
    Calendar getReferenceById(Long id);


}
