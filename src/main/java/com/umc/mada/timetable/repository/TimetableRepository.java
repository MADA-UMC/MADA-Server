package com.umc.mada.timetable.repository;

import com.umc.mada.timetable.domain.Timetable;
import com.umc.mada.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Integer> {
    Optional<Timetable> findTimetableById(int scheduleId);
    List<Timetable> findTimetablesByUserIdAndDateIs(User userId, LocalDate date);
    Optional<Timetable> deleteTimetableByUserIdAndId(User userId, int id);
    Optional<Timetable> findTimetableByUserIdAndId(User userId, int id);
}
