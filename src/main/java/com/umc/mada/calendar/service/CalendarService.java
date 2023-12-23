package com.umc.mada.calendar.service;

import com.umc.mada.calendar.domain.Calendar;
import com.umc.mada.calendar.domain.RepeatCalendar;
import com.umc.mada.calendar.dto.CalendarRequestDto;
import com.umc.mada.calendar.dto.CalendarResponseDto;
import com.umc.mada.calendar.repository.CalendarRepository;
import com.umc.mada.calendar.repository.RepeatCalendarRepository;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final  RepeatCalendarRepository repeatCalendarRepository;
    private final UserRepository userRepository;


    @Autowired
    public CalendarService(CalendarRepository calendarRepository,RepeatCalendarRepository repeatCalendarRepository ,UserRepository userRepository){
        this.calendarRepository = calendarRepository;
        this.userRepository = userRepository;
        this.repeatCalendarRepository = repeatCalendarRepository;
    }

//
//    public Map<String ,Object> getCalendar(Authentication authentication,CalendarRequestDto calendarRequestDto){
//        calendarRepository.find
//    }

    public Map<String,Object> createRepeatCalendar(Authentication authentication , Calendar calendar){
        User user = this.getUser(authentication);
        LocalDate startDate = calendar.getStartDate();
        LocalDate endDate = calendar.getEndDate();
        List<LocalDate> dates=new ArrayList<>();
        List<RepeatCalendar> repeatCalendars = new ArrayList<>();
        if (calendar.getRepeat() == 'D') {
            while (!startDate.isAfter(endDate)) {
                dates.add(startDate);
                startDate = startDate.plusDays(1);
            }

        } else if (calendar.getRepeat() == 'W') {
            int dayOfWeek = startDate.getDayOfWeek();
            startDate = startDate.minusDays(dayOfWeek);
            startDate=startDate.plusDays(calendar.getRepeatInfo());
            if(calendar.getStartDate().isBefore(startDate)){
                dates.add(startDate);
            }
            while (!startDate.isAfter(endDate)) {
                dates.add(startDate);
                startDate = startDate.plusWeeks(1);

            }


        } else if (calendar.getRepeat() == 'M') {
            int dayOfMonth = startDate.getDayOfMonth();
            startDate = startDate.minusDays(dayOfMonth);
            startDate=startDate.plusDays(calendar.getRepeatInfo());
            if(calendar.getStartDate().isBefore(startDate)){
                dates.add(startDate);
            }
            while (!startDate.isAfter(endDate)) {
                dates.add(startDate);
                startDate = startDate.plusMonths(1);

            }


        } else if (calendar.getRepeat() == 'Y'){
            int dayOfYear = startDate.getDayOfYear();
            startDate = startDate.minusDays(dayOfYear);
            startDate = startDate.plusDays(calendar.getRepeatInfo());
            if(calendar.getStartDate().isBefore(startDate)){
                dates.add(startDate);
            }
            while (!startDate.isAfter(endDate)) {
                dates.add(startDate);
                startDate = startDate.plusYears(1);

            }

        }
        for (LocalDate date: dates) {
            RepeatCalendar repeatCalendar = RepeatCalendar.builder()
                    .calendarId(calendar)
                    .date(date)
                    .build();
            repeatCalendarRepository.save((repeatCalendar));
            repeatCalendars.add(repeatCalendar);

        }
        Map<String,Object> map = new LinkedHashMap<>();
        Map<String ,Object> data = new LinkedHashMap<>();
        data.put("startTodoAtMonday",user.isStartTodoAtMonday());
        data.put("calendars",repeatCalendars);
        map.put("data",data);
        return map;
    }
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
        List<Calendar> calendarList = readCalendarsByMonth(calendarRepository.findAllByUser(user).stream().filter(calendar -> !calendar.isExpired()).collect(Collectors.toList()),year,month);
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



    public Map<String,Object> readRepeats(Authentication authentication) {
        User user = this.getUser(authentication);
        List<Calendar> calendarList = calendarRepository.findCalendarsByUserAndRepeatIsNotContaining(user,"No").stream().filter(calendar -> !calendar.isExpired()).collect(Collectors.toList());
        List<CalendarResponseDto> calendarResponseDtoList =  calendarList.stream().map(this::calendarToDto).collect(Collectors.toList());
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String ,Object> data = new LinkedHashMap<>();
        data.put("startTodoAtMonday",user.isStartTodoAtMonday());
        data.put("calendars",calendarResponseDtoList);
        map.put("data",data);
        return map;
    }
    public Map<String, Object> calendarsRead(Authentication authentication) {
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
    public CalendarResponseDto calendarCreate(Authentication authentication, CalendarRequestDto calendarRequestDto) {
        User user = this.getUser(authentication);
        Calendar calendar = this.calendarBuilder(user,calendarRequestDto);
        calendarRepository.save(calendar);
        return this.calendarToDto(calendar);
    }

    public CalendarResponseDto calendarEdit(Authentication authentication, Long id, CalendarRequestDto calendarRequestDto){
        User user = this.getUser(authentication);
        Calendar calendar = calendarRepository.findCalendarByUserAndId(user, id).get();
        Calendar updateCalendar = this.updateCalendar(calendar,calendarRequestDto);
        return this.calendarToDto(updateCalendar);
    }


    public CalendarResponseDto calendarDelete(Authentication authentication, Long id){
        User user = this.getUser(authentication);
        Calendar calendar = calendarRepository.findCalendarByUserAndId(user,id).get();
        calendar.setExpired(true);
        calendarRepository.save(calendar);

        return this.calendarToDto(calendar);
    }


//    public List<Calendar> readCalendarsByDate(List<Calendar> calendarList, Date date){
//        return calendarList.stream()
//                .filter(calendar ->  calendar.getDday() =='N' &&calendar.getStartDate().compareTo(date)<=0 &&calendar.getEndDate().compareTo(date)>=0
//                        ||calendar.getRepeat() == 'D'
//                        ||calendar.getRepeat() == 'W'
//                        && date.toLocalDate().getDayOfWeek().getValue()<=calendar.getStartDate().toLocalDate().getDayOfWeek().getValue()
//                        && date.toLocalDate().getDayOfWeek().getValue()>=calendar.getEndDate().toLocalDate().getDayOfWeek().getValue()
//                        || calendar.getRepeat() =='M'
//                        && date.toLocalDate().getDayOfMonth() <= calendar.getStartDate().toLocalDate().getDayOfMonth()
//                        && date.toLocalDate().getDayOfMonth() >= calendar.getEndDate().toLocalDate().getDayOfMonth()
//                        || calendar.getRepeat()== 'Y'
//                        && date.toLocalDate().getDayOfYear()<=calendar.getStartDate().toLocalDate().getDayOfYear()
//                        && date.toLocalDate().getDayOfYear() >= calendar.getEndDate().toLocalDate().getDayOfYear()
//                )
//                .collect(Collectors.toList());
//    }
//
//    private List<Calendar> readCalendarsByMonth(List<Calendar> calendarList,int year, int month){
//        Date date = new Date(year,month,1);
//        return calendarList.stream()
//                .filter(calendar -> (calendar.getRepeat() == 'D' && calendar.getStartDate().toLocalDate().getMonthValue()<=month&&calendar.getEndDate().toLocalDate().getMonthValue()>=month
//                        &&calendar.getStartDate().toLocalDate().getYear()<=year&&calendar.getEndDate().toLocalDate().getYear()>=year)
//                        ||(calendar.getRepeat() == 'W'
//                        && date.toLocalDate().getDayOfWeek().getValue()<=calendar.getStartDate().toLocalDate().getDayOfWeek().getValue()
//                        && date.toLocalDate().getDayOfWeek().getValue()>=calendar.getEndDate().toLocalDate().getDayOfWeek().getValue())
//                        || (calendar.getRepeat() =='M'
//                        && date.toLocalDate().getDayOfMonth() <= calendar.getStartDate().toLocalDate().getDayOfMonth()
//                        && date.toLocalDate().getDayOfMonth() >= calendar.getEndDate().toLocalDate().getDayOfMonth())
//                        || (calendar.getRepeat() == 'Y'
//                        && date.toLocalDate().getDayOfYear()<=calendar.getStartDate().toLocalDate().getDayOfYear()
//                        && date.toLocalDate().getDayOfYear() >= calendar.getEndDate().toLocalDate().getDayOfYear()))
//                .collect(Collectors.toList());
//    }
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
                .repeat(calendar.getRepeat())
                .repeatInfo(calendar.getRepeatInfo())
                .isExpired(calendar.isExpired())
                .build();
    }
    private Calendar calendarBuilder(User user,CalendarRequestDto calendarRequestDto){
        if (calendarRequestDto.getIsExpired() == null){
            return Calendar.builder()
                    .user(user)
                    .calendarName(calendarRequestDto.getCalendarName())
                    .dday(calendarRequestDto.getDday())
                    .repeat(calendarRequestDto.getRepeat())
                    .memo(calendarRequestDto.getMemo())
                    .startDate(calendarRequestDto.getStartDate())
                    .endDate(calendarRequestDto.getEndDate())
                    .startTime(calendarRequestDto.getStartTime())
                    .endTime(calendarRequestDto.getEndTime())
                    .color(calendarRequestDto.getColor())
                    .repeatInfo(calendarRequestDto.getRepeatInfo())
                    .build();
        }
        return Calendar.builder()
                .user(user)
                .calendarName(calendarRequestDto.getCalendarName())
                .dday(calendarRequestDto.getDday())
                .repeat(calendarRequestDto.getRepeat())
                .memo(calendarRequestDto.getMemo())
                .startDate(calendarRequestDto.getStartDate())
                .endDate(calendarRequestDto.getEndDate())
                .startTime(calendarRequestDto.getStartTime())
                .endTime(calendarRequestDto.getEndTime())
                .color(calendarRequestDto.getColor())
                .repeatInfo(calendarRequestDto.getRepeatInfo())
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
            calendar.setRepeat(calendarRequestDto.getRepeat());
            calendar.setRepeatInfo(calendarRequestDto.getRepeatInfo());
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
        calendar.setRepeat(calendarRequestDto.getRepeat());
        calendar.setRepeatInfo(calendarRequestDto.getRepeatInfo());
        calendar.setExpired(calendarRequestDto.getIsExpired());
        calendarRepository.save(calendar);
        return calendar;
    }


}
