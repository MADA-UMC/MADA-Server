package com.umc.mada.timetable.repository;

import com.umc.mada.timetable.domain.Timetable;
import com.umc.mada.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.umc.mada.timetable.domain.DayOfWeek;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Integer> {
    List<Timetable> findTimetablesByUserIdAndDateIs(User userId, LocalDate date);
    Optional<Timetable> findTimetableByUserIdAndId(User userId, int id);
    List<Timetable> findTimetablesByUserIdAndDayOfWeekIsNot(User userId, DayOfWeek dayOfWeek);
    List<Timetable> findTimetablesByUserIdAndDateIsAndDayOfWeek(User userId, LocalDate date, DayOfWeek dayOfWeek);
    List<Timetable> findTimetablesByUserIdAndDayOfWeekAndIsDeletedIsFalse(User userId, DayOfWeek dayOfWeek);

}
