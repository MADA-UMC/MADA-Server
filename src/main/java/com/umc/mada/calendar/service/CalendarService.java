package com.umc.mada.calendar.service;

import com.umc.mada.calendar.domain.Calendar;
import com.umc.mada.calendar.dto.CalendarRequestDto;
import com.umc.mada.calendar.dto.CalendarResponseDto;
import com.umc.mada.calendar.repository.CalendarRepository;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j //로그처리
@Transactional //트렌젝션 처리
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    @Autowired
    public  CalendarService(CalendarRepository calendarRepository, UserRepository userRepository){
        this.calendarRepository = calendarRepository;
        this.userRepository = userRepository;

    }
    //동일 이름의 일정이 동일한 날짜에 있는지 검증
    //캘린더 생성코드
    public CalendarResponseDto createCalendar(Authentication authentication, CalendarRequestDto calendarRequestDto){
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        Calendar calendar = Calendar.builder()
                //User Entity 부재
                .uid(user.getId())
                .calenderName(calendarRequestDto.getCalenderName())
                .d_day(calendarRequestDto.getD_day())
                .repeat(calendarRequestDto.getRepeat())
                .memo(calendarRequestDto.getMemo())
                .startDate(calendarRequestDto.getStartDate())
                .endDate(calendarRequestDto.getEndDate())
                .build();
        calendarRepository.save(calendar);
        return new CalendarResponseDto(calendarRequestDto.getCalenderName(),calendarRequestDto.getStartDate(),calendarRequestDto.getEndDate(),calendarRequestDto.getD_day(), calendarRequestDto.getColor());
    }
    public CalendarResponseDto editCalendar(Authentication authentication,Long id,CalendarRequestDto calendarRequestDto){
        Calendar calendar = calendarRepository.findCalendarById(id);
        calendar.setMemo(calendarRequestDto.getMemo());
        calendar.setStartDate(calendarRequestDto.getStartDate());
        calendar.setEndDate(calendarRequestDto.getEndDate());
        calendar.setCalenderName(calendarRequestDto.getCalenderName());
        calendar.setColor(calendar.getColor());
        calendarRepository.save(calendar);
        return new CalendarResponseDto(calendarRequestDto.getCalenderName(),calendarRequestDto.getStartDate(),calendarRequestDto.getEndDate(),calendarRequestDto.getD_day(), calendarRequestDto.getColor());
    }
    public List<CalendarResponseDto> readCalendars(Authentication authentication, int month) {
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        List<Calendar> calendarList = calendarRepository.findAllByUid(user.getId());
        List<CalendarResponseDto> calendarResponseDtoList = new ArrayList<>();
        for (Calendar calendar: calendarList) {
            calendarResponseDtoList.add(CalendarResponseDto.builder().
                    calenderName(calendar.getCalenderName()).
                    startDate(calendar.getStartDate()).
                    endDate(calendar.getEndDate()).
                    d_day(calendar.getD_day()).
                    color(calendar.getColor()).build());
        }
        return calendarResponseDtoList;
    }
    public boolean deleteCalendar(Authentication authentication, Long id){
        return calendarRepository.deleteCalendarById(id);
    }
    public boolean tooManyD_dayExists(Authentication authentication){
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        return calendarRepository.findAllByUid(user.getId()).size() >= 3;

    }
    public boolean calendarNameExist(Authentication authentication ,CalendarRequestDto calendarRequestDto){
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        return calendarRepository.existsCalendarByUidAndEndDateBetweenAndCalenderName(
                user.getId()
                , calendarRequestDto.getStartDate()
                , calendarRequestDto.getEndDate()
                ,calendarRequestDto.getCalenderName()
                );
    }
        //return calendarResponseDtoList;
    //}
    public void deleteCalender(Long id) {
        calendarRepository.deleteById(id);
    }

}
