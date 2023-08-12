package com.umc.mada.calendar.repository;

import com.umc.mada.calendar.domain.Calendar;
import com.umc.mada.user.domain.User;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar,Long> {
    //Optional<Calendar> findCalendarById(Long id); 캘린더 고유 아이디로 캘린더 조회
    List<Calendar> findAllByUser(User user);
    List<Calendar> findAllByUserAndDday(User user,char dday);
    Calendar findCalendarByUserAndId(User user, Long id);

    Calendar findCalendarById(Long id);
    void deleteCalendarById(Long id);
    boolean existsCalendarByUserAndEndDateBetweenAndCalenderName(User user, Date startDate, Date endDate, String calendarName);
    @Override
    Calendar getReferenceById(Long id);


}
