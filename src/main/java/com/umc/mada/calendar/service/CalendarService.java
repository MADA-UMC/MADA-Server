package com.umc.mada.calendar.service;

import com.umc.mada.calendar.domain.Calendar;
import com.umc.mada.calendar.domain.ManyD_dayException;
import com.umc.mada.calendar.domain.SameCalendarNameExist;
import com.umc.mada.calendar.dto.CalendarRequestDto;
import com.umc.mada.calendar.dto.CalendarResponseDto;
import com.umc.mada.calendar.repository.CalendarRepository;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j //로그처리
@Transactional //트렌젝션 처리
public class CalendarService {
    private final CalendarRepository calendarRepository;



    private final UserRepository userRepository;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository, UserRepository userRepository){
        this.calendarRepository = calendarRepository;
        this.userRepository = userRepository;
    }

    public Map<String, Object> readDday(Authentication authentication){
        User user = this.getUser(authentication);
        List<Calendar> calendarList = calendarRepository.findAllByUserAndDday(user,'Y');
        List<CalendarResponseDto> calendarResponseDtoList = new ArrayList<>();

        for (Calendar calendar: calendarList) {
            calendarResponseDtoList.add(this.calendarToDto(calendar));
        }
        Map<String,Object> map = new HashMap<>();
        Map<String ,Object> data = new HashMap<>();
        data.put("startTodoAtMonday",user.isStartTodoAtMonday());
        data.put("calendar",calendarResponseDtoList);
        map.put("data",data);
        return map;
    }

    public Map<String, Object> readMonthCalendar(Authentication authentication, int year,int month){
        User user = this.getUser(authentication);
        List<Calendar> calendarList = readCalendarsByMonth(calendarRepository.findAllByUser(user),year,month);
        List<CalendarResponseDto> calendarResponseDtoList = new ArrayList<>();
        for (Calendar calendar: calendarList) {
            calendarResponseDtoList.add(this.calendarToDto(calendar));
        }
        Map<String,Object> map = new HashMap<>();
        Map<String ,Object> data = new HashMap<>();
        data.put("startTodoAtMonday",user.isStartTodoAtMonday());
        data.put("calendar",calendarResponseDtoList);
        map.put("data",data);
        return map;
    }

    public Map<String,Object> calendarsReadByDate(Authentication authentication,Date date){
        User user = this.getUser(authentication);

        List<Calendar> calendarList = readCalendarsByDate(calendarRepository.findAllByUser(user),date);
        List<CalendarResponseDto> calendarResponseDtoList = new ArrayList<>();
        for (Calendar calendar: calendarList) {
            calendarResponseDtoList.add(this.calendarToDto(calendar));
        }
        Map<String, Object> map = new HashMap<>();
        Map<String ,Object> data = new HashMap<>();
        data.put("startTodoAtMonday",user.isStartTodoAtMonday());
        data.put("calendar",calendarResponseDtoList);
        map.put("data",data);
        return map;
    }
    public Map<String, Object> calendarsRead(Authentication authentication) {
        User user = this.getUser(authentication);
        List<Calendar> calendarList = calendarRepository.findAllByUser(user);
        List<CalendarResponseDto> calendarResponseDtoList = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        for (Calendar calendar: calendarList) {
            calendarResponseDtoList.add(this.calendarToDto(calendar));
        }
        Map<String ,Object> data = new HashMap<>();
        data.put("startTodoAtMonday",user.isStartTodoAtMonday());
        data.put("calendar",calendarResponseDtoList);
        map.put("data",data);
        return map;
    }
    //동일 이름의 일정이 동일한 날짜에 있는지 검증
    //캘린더 생성코드
    public CalendarResponseDto calendarCreate(Authentication authentication, CalendarRequestDto calendarRequestDto) {
        User user = this.getUser(authentication);
       /* if (calendarRequestDto.getDday() == 'Y' && tooManyD_dayExists(authentication)) {
            throw new ManyD_dayException("D_day가 3개 이상 존재합니다");
        }
        if(calendarNameExist(authentication,calendarRequestDto)){
            throw new SameCalendarNameExist("기간 중에 동일한 이름의 캘린더가 존재합니다");
        }*/
        Calendar calendar = this.calendarBuilder(user,calendarRequestDto);
        calendarRepository.save(calendar);
        return this.calendarToDto(calendar);
    }

    public CalendarResponseDto calendarEdit(Authentication authentication, Long id, CalendarRequestDto calendarRequestDto){
        User user = this.getUser(authentication);
        Calendar calendar = calendarRepository.findCalendarByUserAndId(user, id);
        Calendar updateCalendar = this.updateCalendar(calendar,calendarRequestDto);
        return this.calendarToDto(updateCalendar);
    }


    public CalendarResponseDto calendarDelete(Authentication authentication, Long id){
        User user = this.getUser(authentication);
        Calendar calendar = calendarRepository.findCalendarByUserAndId(user,id);
        CalendarResponseDto calendarResponseDto =  this.calendarToDto(calendar);
        calendarRepository.deleteCalendarById(id);
        return calendarResponseDto;
    }

    public boolean tooManyD_dayExists(Authentication authentication){
        User user = this.getUser(authentication);
        return calendarRepository.findAllByUser(user).size() >= 3;
    }


    public List<Calendar> readCalendarsByDate(List<Calendar> calendarList, Date date){
        return calendarList.stream()
                .filter(calendar ->  calendar.getDday() =='N' &&calendar.getStartDate().compareTo(date)<=0 &&calendar.getEndDate().compareTo(date)>=0 ||calendar.getRepeat() == "Day"
                        ||calendar.getRepeat() == "Week"
                        && date.toLocalDate().getDayOfWeek().getValue()<=calendar.getStartDate().toLocalDate().getDayOfWeek().getValue()
                        && date.toLocalDate().getDayOfWeek().getValue()>=calendar.getEndDate().toLocalDate().getDayOfWeek().getValue()
                        || calendar.getRepeat() =="Month"
                        && date.toLocalDate().getDayOfMonth() <= calendar.getStartDate().toLocalDate().getDayOfMonth()
                        && date.toLocalDate().getDayOfMonth() >= calendar.getEndDate().toLocalDate().getDayOfMonth()
                        || calendar.getRepeat()=="Year"
                        && date.toLocalDate().getDayOfYear()<=calendar.getStartDate().toLocalDate().getDayOfYear()
                        && date.toLocalDate().getDayOfYear() >= calendar.getEndDate().toLocalDate().getDayOfYear()
                )
                .collect(Collectors.toList());
    }
    private List<Calendar> readCalendarsByMonth(List<Calendar> calendarList,int year, int month){
        Date date = new Date(year,month,1);
        return calendarList.stream()
                .filter(calendar -> calendar.getStartDate().toLocalDate().getMonthValue()<=month&&calendar.getEndDate().toLocalDate().getMonthValue()>=month
                        &&calendar.getStartDate().toLocalDate().getYear()<=year&&calendar.getEndDate().toLocalDate().getYear()>=year
                        ||calendar.getRepeat() == "Day"
                        ||calendar.getRepeat() == "Week"
                        && date.toLocalDate().getDayOfWeek().getValue()<=calendar.getStartDate().toLocalDate().getDayOfWeek().getValue()
                        && date.toLocalDate().getDayOfWeek().getValue()>=calendar.getEndDate().toLocalDate().getDayOfWeek().getValue()
                        || calendar.getRepeat() =="Month"
                        && date.toLocalDate().getDayOfMonth() <= calendar.getStartDate().toLocalDate().getDayOfMonth()
                        && date.toLocalDate().getDayOfMonth() >= calendar.getEndDate().toLocalDate().getDayOfMonth()
                        || calendar.getRepeat() == "Year"
                        && date.toLocalDate().getDayOfYear()<=calendar.getStartDate().toLocalDate().getDayOfYear()
                        && date.toLocalDate().getDayOfYear() >= calendar.getEndDate().toLocalDate().getDayOfYear())
                .collect(Collectors.toList());
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
                .repeat(calendar.getRepeat())
                .build();
    }
    private Calendar calendarBuilder(User user,CalendarRequestDto calendarRequestDto){
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
                .build();
    }
    private Calendar updateCalendar(Calendar calendar, CalendarRequestDto calendarRequestDto){
        calendar.setMemo(calendarRequestDto.getMemo());
        calendar.setStartDate(calendarRequestDto.getStartDate());
        calendar.setEndDate(calendarRequestDto.getEndDate());
        calendar.setCalendarName(calendarRequestDto.getCalendarName());
        calendar.setDday(calendarRequestDto.getDday());
        calendar.setColor(calendarRequestDto.getColor());
        calendar.setEndTime(calendarRequestDto.getEndTime());
        calendar.setStartTime(calendarRequestDto.getStartTime());
        calendar.setRepeat(calendarRequestDto.getRepeat());
        calendarRepository.save(calendar);
        return calendar;
    }

}
