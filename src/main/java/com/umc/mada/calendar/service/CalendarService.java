package com.umc.mada.calendar.service;

import com.umc.mada.calendar.domain.Calendar;
import com.umc.mada.calendar.dto.CalendarRequestDto;
import com.umc.mada.calendar.dto.CalendarResponseDto;
import com.umc.mada.calendar.repository.CalendarRepository;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;

    private final UserRepository userRepository;


    @Autowired
    public CalendarService(CalendarRepository calendarRepository ,UserRepository userRepository){
        this.calendarRepository = calendarRepository;
        this.userRepository = userRepository;
    }

//
//    public Map<String ,Object> getCalendar(Authentication authentication,CalendarRequestDto calendarRequestDto){
//        calendarRepository.find
//    }


    public Map<String, Object> readDday(Authentication authentication){
        User user = this.getUser(authentication);
        List<Calendar> calendarList = calendarRepository.findAllByUserAndDday(user,'Y').stream().filter(calendar -> !calendar.isExpired()).collect(Collectors.toList());;
        List<CalendarResponseDto> calendarResponseDtoList = new ArrayList<>();

        for (Calendar calendar: calendarList) {
            calendarResponseDtoList.add(this.calendarToDto(calendar));
        }
        Map<String,Object> map = new LinkedHashMap<>();
        Map<String ,Object> data = new LinkedHashMap<>();
        data.put("startTodoAtMonday",user.isStartTodoAtMonday());
        data.put("calendars",calendarResponseDtoList);
        map.put("data",data);
        return map;
    }

    public Map<String, Object> readMonthCalendar(Authentication authentication, int year,int month){
        User user = this.getUser(authentication);

        List<Calendar> calendarList = calendarRepository.findCalendarMonth(user,year,month);
        List<CalendarResponseDto> calendarResponseDtoList = new ArrayList<>();

        for (Calendar calendar: calendarList) {
            calendarResponseDtoList.add(this.calendarToDto(calendar));
        }

        Map<String,Object> map = new LinkedHashMap<>();
        Map<String ,Object> data = new LinkedHashMap<>();
        data.put("startTodoAtMonday",user.isStartTodoAtMonday());
        data.put("calendars",calendarResponseDtoList);
        map.put("data",data);
        return map;
    }
    public Map<String,Object> readDayCalendars(Authentication authentication, LocalDate localDate){
        User user = this.getUser(authentication);
        List<Calendar> calendarList = readCalendarsByDate(calendarRepository.findAllByUser(user).stream().filter(calendar -> !calendar.isExpired()).collect(Collectors.toList()),localDate);


        List<CalendarResponseDto> calendarResponseDtoList = new ArrayList<>();

        for (Calendar calendar: calendarList) {
            calendarResponseDtoList.add(this.calendarToDto(calendar));
        }

        Map<String,Object> map = new LinkedHashMap<>();
        Map<String ,Object> data = new LinkedHashMap<>();
        data.put("startTodoAtMonday",user.isStartTodoAtMonday());
        data.put("calendars",calendarResponseDtoList);

        map.put("data",data);
        return map;
    }


//    public Map<String,Object> readRepeats(Authentication authentication) {
//        User user = this.getUser(authentication);
//        List<Calendar> calendarList = calendarRepository.findCalendarsByUserAndRepeatIsNotContaining(user,"No").stream().filter(calendar -> !calendar.isExpired()).collect(Collectors.toList());
//        List<CalendarResponseDto> calendarResponseDtoList =  calendarList.stream().map(this::calendarToDto).collect(Collectors.toList());
//        Map<String, Object> map = new LinkedHashMap<>();
//        Map<String ,Object> data = new LinkedHashMap<>();
//        data.put("startTodoAtMonday",user.isStartTodoAtMonday());
//        data.put("calendars",calendarResponseDtoList);
//        map.put("data",data);
//        return map;
//    }
    public Map<String, Object> readCalendars(Authentication authentication) {
        User user = this.getUser(authentication);
        List<Calendar> calendarList = calendarRepository.findAllByUser(user).stream().filter(calendar -> !calendar.isExpired()).collect(Collectors.toList());;
        List<CalendarResponseDto> calendarResponseDtoList = new ArrayList<>();

        Map<String,Object> map = new LinkedHashMap<>();
        for (Calendar calendar: calendarList) {
            calendarResponseDtoList.add(this.calendarToDto(calendar));

        }
        Map<String ,Object> data = new LinkedHashMap<>();
        data.put("startTodoAtMonday",user.isStartTodoAtMonday());
        data.put("calendars",calendarResponseDtoList);

        map.put("data",data);
        return map;
    }
    //동일 이름의 일정이 동일한 날짜에 있는지 검증
    //캘린더 생성코드
    public Map<String,Object> createCalendar(Authentication authentication, CalendarRequestDto calendarRequestDto) {
        User user = this.getUser(authentication);
        Calendar calendar = this.calendarBuilder(user,calendarRequestDto);
        Map<String,Object> map = new LinkedHashMap<>();
        Map<String,Object> data = new LinkedHashMap<>();

        calendarRepository.save(calendar);

        CalendarResponseDto calendarResponseDto = this.calendarToDto(calendar);

        data.put("calendar", calendarResponseDto);
        map.put("data",data);
        return map;
    }

    public Map<String,Object> editCalendar(Authentication authentication, Long id, CalendarRequestDto calendarRequestDto){
        User user = this.getUser(authentication);
        Map<String,Object> data = new LinkedHashMap<>();
        Calendar updateCalendar;
        Calendar calendar = calendarRepository.findCalendarByUserAndId(user, id).get();

            updateCalendar = this.updateCalendar(calendar,calendarRequestDto);


            data.put("calendars", this.calendarToDto(updateCalendar));

            return data;


    }


    public Map<String,Object> deleteCalendar(Authentication authentication, Long id){
        User user = this.getUser(authentication);
        Calendar calendar = calendarRepository.findCalendarByUserAndId(user,id).get();
        Map<String,Object> data = new LinkedHashMap<>();
        calendar.setExpired(true);

        //모두 제거


        calendarRepository.save(calendar);
        data.put("calendars",this.calendarToDto(calendar));

        return data;

    }


    public List<Calendar> readCalendarsByDate(List<Calendar> calendarList, LocalDate date){
        List<Calendar> calendars = calendarList.stream()
                .filter(calendar -> calendar.getStartDate().isBefore(date)&&calendar.getEndDate().isAfter(date))
                .collect(Collectors.toList());
        return calendars;
    }





    private User getUser(Authentication authentication) throws NoSuchElementException {
        try{
            Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
            return userOptional.get();
        }catch (RuntimeException e){
            throw new NoSuchElementException();
        }
    }


    private CalendarResponseDto calendarToDto(Calendar calendar){
        return CalendarResponseDto.builder()
                .calendarId(calendar.getId())
                .calendarName(calendar.getCalendarName())
                .startDate(calendar.getStartDate())
                .endDate(calendar.getEndDate())
                .startTime(calendar.getStartTime())
                .endTime(calendar.getEndTime())
                .color(calendar.getColor())
                .dday(calendar.getDday())
                .memo(calendar.getMemo())
                .isExpired(calendar.isExpired())
                .build();
    }
    private Calendar calendarBuilder(User user,CalendarRequestDto calendarRequestDto){
        if (calendarRequestDto.getIsExpired() == null){
            return Calendar.builder()
                    .user(user)
                    .calendarName(calendarRequestDto.getCalendarName())
                    .dday(calendarRequestDto.getDday())

                    .memo(calendarRequestDto.getMemo())
                    .startDate(calendarRequestDto.getStartDate())
                    .endDate(calendarRequestDto.getEndDate())
                    .startTime(calendarRequestDto.getStartTime())
                    .endTime(calendarRequestDto.getEndTime())
                    .color(calendarRequestDto.getColor())

                    .build();
        }
        return Calendar.builder()
                .user(user)
                .calendarName(calendarRequestDto.getCalendarName())
                .dday(calendarRequestDto.getDday())

                .memo(calendarRequestDto.getMemo())
                .startDate(calendarRequestDto.getStartDate())
                .endDate(calendarRequestDto.getEndDate())
                .startTime(calendarRequestDto.getStartTime())
                .endTime(calendarRequestDto.getEndTime())
                .color(calendarRequestDto.getColor())

                .isExpired(calendarRequestDto.getIsExpired())
                .build();
    }
    private Calendar updateCalendar(Calendar calendar, CalendarRequestDto calendarRequestDto){

        if(calendarRequestDto.getIsExpired() == null){
            calendar.setMemo(calendarRequestDto.getMemo());
            calendar.setStartDate(calendarRequestDto.getStartDate());
            calendar.setEndDate(calendarRequestDto.getEndDate());
            calendar.setCalendarName(calendarRequestDto.getCalendarName());
            calendar.setDday(calendarRequestDto.getDday());
            calendar.setColor(calendarRequestDto.getColor());
            calendar.setEndTime(calendarRequestDto.getEndTime());
            calendar.setStartTime(calendarRequestDto.getStartTime());

            calendarRepository.save(calendar);
            return calendar;
        }
        calendar.setMemo(calendarRequestDto.getMemo());
        calendar.setStartDate(calendarRequestDto.getStartDate());
        calendar.setEndDate(calendarRequestDto.getEndDate());
        calendar.setCalendarName(calendarRequestDto.getCalendarName());
        calendar.setDday(calendarRequestDto.getDday());
        calendar.setColor(calendarRequestDto.getColor());
        calendar.setEndTime(calendarRequestDto.getEndTime());
        calendar.setStartTime(calendarRequestDto.getStartTime());

        calendar.setExpired(calendarRequestDto.getIsExpired());
        calendarRepository.save(calendar);
        return calendar;
    }


}
