package com.umc.mada.attendance.service;

import com.umc.mada.attendance.domain.Attendance;
import com.umc.mada.attendance.repository.AttendanceRepository;
import com.umc.mada.user.domain.User;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public int getAttendanceCountForMonth(User user, int month) {
        Optional<Attendance> attendance = attendanceRepository.findByUserAndMonth(user, month);

        return attendance.map(Attendance::getCount).orElse(0);
    }

    public void markAttendance(User user) {
        int currentMonth = LocalDate.now().getMonthValue();

        Optional<Attendance> attendance = attendanceRepository.findByUserAndMonth(user, currentMonth);
        if (attendance.isPresent()) {
            Attendance existingAttendance = attendance.get();
            existingAttendance.setCount(existingAttendance.getCount() + 1);
        } else {
            Attendance newAttendance = new Attendance();
            newAttendance.setUser(user);
            newAttendance.setCount(1);
            attendanceRepository.save(newAttendance);
        }
    }
}

