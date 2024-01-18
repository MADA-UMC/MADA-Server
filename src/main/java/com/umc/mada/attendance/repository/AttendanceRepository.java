package com.umc.mada.attendance.repository;

import com.umc.mada.attendance.domain.Attendance;
import com.umc.mada.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByUserAndMonth(User user, int month);
}
