package com.umc.mada.calendar.service;

import com.umc.mada.calendar.domain.Calendar;
import com.umc.mada.calendar.dto.CalendarRequestDto;
import com.umc.mada.calendar.dto.CalendarResponseDto;
import com.umc.mada.calendar.repository.CalendarRepository;
import com.umc.mada.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j //로그처리
@Transactional //트렌젝션 처리
public class CalendarService {
    private final CalendarRepository calendarRepository;
    @Autowired
    public  CalendarService(CalendarRepository calendarRepository){
        this.calendarRepository = calendarRepository;

    }
    //동일 이름의 일정이 동일한 날짜에 있는지 검증

    //캘린더 생성코드
    public CalendarResponseDto createCalendar(CalendarRequestDto calenderDto){
        Calendar calendar = Calendar.builder()
                //User Entity 부재
                //.user_id(userRepository.getReferenceById(calenderDto.getUser_id()))
                .calender_name(calenderDto.getCalender_name())
                .d_day(calenderDto.getD_day())
                .repeat(calenderDto.getRepeat())
                .memo(calenderDto.getMemo())
                .start_date(calenderDto.getStart_date())
                .end_date(calenderDto.getEnd_date())
                .build();
        calendarRepository.save(calendar);
        return new CalendarResponseDto(calenderDto.getCalender_name(),calenderDto.getStart_date(),calenderDto.getEnd_date(),calenderDto.getD_day(), calenderDto.getColor());
    }
    public CalendarResponseDto editCalendar(Long id,CalendarRequestDto calendarRequestDto){
        Calendar calendar = calendarRepository.getReferenceById(id);
        calendar.setMemo(calendarRequestDto.getMemo());
        calendar.setStart_date(calendarRequestDto.getStart_date());
        calendar.setEnd_date(calendarRequestDto.getEnd_date());
        calendar.setCalender_name(calendarRequestDto.getCalender_name());
        calendar.setColor(calendar.getColor());
        calendarRepository.save(calendar);
        return new CalendarResponseDto(calendarRequestDto.getCalender_name(),calendarRequestDto.getStart_date(),calendarRequestDto.getEnd_date(),calendarRequestDto.getD_day(), calendarRequestDto.getColor());
    }
    public List<CalendarResponseDto> readCalendars(Long uid){
        List<Calendar> calendarList = calendarRepository.findAllByUid(uid);
        List<CalendarResponseDto> calendarResponseDtoList = new ArrayList<>();
        for (Calendar calendar: calendarList) {
            calendarResponseDtoList.add(CalendarResponseDto.builder().
                            calender_name(calendar.getCalender_name()).
                            start_date(calendar.getStart_date()).
                            end_date(calendar.getEnd_date()).
                            d_day(calendar.getD_day()).
                            color(calendar.getColor()).build());
        //}
        //return calendarResponseDtoList;
    //}
    public void deleteCalender(Long id) {
        calendarRepository.deleteById(id);
    }

    public boolean isExistCalendarName(Long uid,CalendarRequestDto calendarRequestDto){
        String name = calendarRequestDto.getCalender_name();
        Timestamp start_date = calendarRequestDto.getStart_date();
        Timestamp end_date = calendarRequestDto.getEnd_date();
        if(true){
            return true;
        }
        return false;
    }

    public boolean alreadyManyDDay(Long uid){
        //List<Calendar> calendarList = calendarRepository.findAllByUser_idAndD_dayTrue(uid);
        if(true)//calendarList.size() >= 3)
        {
            return false;
        }
        return true;
    }

}
