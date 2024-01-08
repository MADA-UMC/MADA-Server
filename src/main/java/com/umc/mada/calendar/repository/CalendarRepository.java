package com.umc.mada.calendar.repository;

import com.umc.mada.calendar.domain.Calendar;
import com.umc.mada.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar,Long> {
    //Optional<Calendar> findCalendarById(Long id); 캘린더 고유 아이디로 캘린더 조회
    List<Calendar> findAllByUser(User user);
    List<Calendar> findAllByUserAndDday(User user,char dday);
    Optional<Calendar> findCalendarByUserAndId(User user, Long id);
    List<Calendar> findCalendarsByUserAndStartDateGreaterThanEqualAndEndDateLessThanEqual(User user, LocalDate start_date, LocalDate end_date );
    List<Calendar> findCalendarsByUserAndRepeatIsNotContaining(User user,String repeat);


}
