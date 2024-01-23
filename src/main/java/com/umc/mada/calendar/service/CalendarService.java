package com.umc.mada.calendar.service;

import com.umc.mada.calendar.domain.Calendar;
import com.umc.mada.calendar.domain.RepeatCalendar;
import com.umc.mada.calendar.dto.CalendarRequestDto;
import com.umc.mada.calendar.dto.CalendarResponseDto;
import com.umc.mada.calendar.dto.RepeatCalendarResponseDto;
import com.umc.mada.calendar.repository.CalendarRepository;
import com.umc.mada.calendar.repository.RepeatCalendarRepository;
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

    public List<RepeatCalendarResponseDto> createRepeatCalendar( Calendar calendar){

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
            int dayOfWeek = startDate.getDayOfWeek().getValue();
            startDate = startDate.minusDays(dayOfWeek);
            startDate=startDate.plusDays(calendar.getRepeatInfo());
//            if(calendar.getStartDate().isBefore(startDate)){
//                dates.add(startDate);
//            }
            while (!startDate.isAfter(endDate)) {
                dates.add(startDate);
                startDate = startDate.plusWeeks(1);

            }


        } else if (calendar.getRepeat() == 'M') {
            int dayOfMonth = startDate.getDayOfMonth();
            startDate = startDate.minusDays(dayOfMonth);
            startDate=startDate.plusDays(calendar.getRepeatInfo());
//            if(calendar.getStartDate().isBefore(startDate)){
//                dates.add(startDate);
//            }
            while (!startDate.isAfter(endDate)) {
                dates.add(startDate);
                startDate = startDate.plusMonths(1);

            }


        } else if (calendar.getRepeat() == 'Y'){
            int dayOfYear = startDate.getDayOfYear();
            startDate = startDate.minusDays(dayOfYear);
            startDate = startDate.plusDays(calendar.getRepeatInfo());

            while (!startDate.isAfter(endDate)) {
                dates.add(startDate);
                startDate = startDate.plusYears(1);

            }

        }
        for (LocalDate date: dates) {
            RepeatCalendar repeatCalendar = RepeatCalendar.builder()
                    .calendarId(calendar)
                    .date(date)
                    .isExpired(false)
                    .build();
            repeatCalendarRepository.save((repeatCalendar));
            repeatCalendars.add(repeatCalendar);

        }
        List<RepeatCalendarResponseDto> result= new ArrayList<>();
        for (RepeatCalendar repeatCalendar: repeatCalendars){
            result.add(repeatCalendarToDto(repeatCalendar));
        }
        return result;
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
        LocalDate start_date = LocalDate.of(year,month,1);

        int days = start_date.lengthOfMonth();
        LocalDate end_date = LocalDate.of(year,month,days);
        List<Calendar> calendarList = calendarRepository.findCalendarsByUserAndStartDateGreaterThanEqualAndEndDateLessThanEqual(user,start_date,end_date);
        List<RepeatCalendar> repeatCalendarList = readRepeatCalendars(calendarList).stream().filter(repeatCalendar -> repeatCalendar.getDate().getMonthValue() == month).collect(Collectors.toList());

        List<CalendarResponseDto> calendarResponseDtoList = new ArrayList<>();
        List<RepeatCalendarResponseDto> repeatCalendarResponseDtoList = new ArrayList<>();
        for (Calendar calendar: calendarList) {
            calendarResponseDtoList.add(this.calendarToDto(calendar));
        }
        for(RepeatCalendar repeatCalendar : repeatCalendarList ){
            if(repeatCalendar.getDate().isAfter(end_date) && repeatCalendar.getDate().isBefore(start_date)){
                continue;
            }
            else {
                repeatCalendarResponseDtoList.add(this.repeatCalendarToDto(repeatCalendar));
            }
        }
        Map<String,Object> map = new LinkedHashMap<>();
        Map<String ,Object> data = new LinkedHashMap<>();
        data.put("startTodoAtMonday",user.isStartTodoAtMonday());
        data.put("calendars",calendarResponseDtoList);
        if(repeatCalendarList.isEmpty())
            data.put("repeat_calendars" , "error");
        else
            data.put("repeat_calendars",repeatCalendarResponseDtoList);
        map.put("data",data);
        return map;
    }
    public Map<String,Object> readDayCalendars(Authentication authentication, LocalDate localDate){
        User user = this.getUser(authentication);
        List<Calendar> calendarList = readCalendarsByDate(calendarRepository.findAllByUser(user).stream().filter(calendar -> !calendar.isExpired()).collect(Collectors.toList()),localDate);
        List<RepeatCalendar> repeatCalendarList = readRepeatCalendars(calendarList);

        List<CalendarResponseDto> calendarResponseDtoList = new ArrayList<>();
        List<RepeatCalendarResponseDto> repeatCalendarResponseDtoList = new ArrayList<>();
        for (Calendar calendar: calendarList) {
            calendarResponseDtoList.add(this.calendarToDto(calendar));
        }
        for(RepeatCalendar repeatCalendar : repeatCalendarList ){
            repeatCalendarResponseDtoList.add(this.repeatCalendarToDto(repeatCalendar));
        }
        Map<String,Object> map = new LinkedHashMap<>();
        Map<String ,Object> data = new LinkedHashMap<>();
        data.put("startTodoAtMonday",user.isStartTodoAtMonday());
        data.put("calendars",calendarResponseDtoList);
        data.put("repeats",repeatCalendarResponseDtoList);
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
        List<RepeatCalendarResponseDto> repeatCalendarResponseDtoList = new ArrayList<>();
        Map<String,Object> map = new LinkedHashMap<>();
        for (Calendar calendar: calendarList) {
            calendarResponseDtoList.add(this.calendarToDto(calendar));
            if (calendar.getRepeat() != 'N'){
                List<RepeatCalendar> repeats = repeatCalendarRepository.readRepeatCalendarsByCalendarIdAndIsExpiredIsFalse(calendar);
                for (RepeatCalendar r: repeats){
                    repeatCalendarResponseDtoList.add(repeatCalendarToDto(r));
                }
            }
        }
        Map<String ,Object> data = new LinkedHashMap<>();
        data.put("startTodoAtMonday",user.isStartTodoAtMonday());
        data.put("calendars",calendarResponseDtoList);
        data.put("repeats",repeatCalendarResponseDtoList);
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
        List<RepeatCalendarResponseDto> repeatCalendarResponseDtoList = new ArrayList<>();
        calendarRepository.save(calendar);
        if(calendar.getRepeat()!='N'){
            repeatCalendarResponseDtoList = createRepeatCalendar(calendar);
        }
        CalendarResponseDto calendarResponseDto = this.calendarToDto(calendar);
        data.put("repeats" ,repeatCalendarResponseDtoList);
        data.put("calendar", calendarResponseDto);
        map.put("data",data);
        return map;
    }

    public Map<String,Object> editCalendar(Authentication authentication, Long id, CalendarRequestDto calendarRequestDto){
        User user = this.getUser(authentication);
        Map<String,Object> data = new LinkedHashMap<>();
        Calendar updateCalendar;
        Calendar calendar = calendarRepository.findCalendarByUserAndId(user, id).get();
        if(calendar.getStartDate().isEqual(calendarRequestDto.getStartDate())  && calendar.getEndDate().isEqual(calendarRequestDto.getEndDate())){
            updateCalendar = this.updateCalendar(calendar,calendarRequestDto);
            data.put("calendars", this.calendarToDto(updateCalendar));
            return data;
        }
        else{
            updateCalendar = this.updateCalendar(calendar,calendarRequestDto);
            List<RepeatCalendar> repeats = repeatCalendarRepository.readRepeatCalendarsByCalendarIdAndIsExpiredIsFalse(calendar);
            List<RepeatCalendarResponseDto> old = new ArrayList<>();
            List<RepeatCalendarResponseDto> updated;
            for (RepeatCalendar r: repeats){
                r.setIsExpired(true);
                repeatCalendarRepository.save(r);
                old.add(repeatCalendarToDto(r));
            }
            updated= createRepeatCalendar(calendar);
            data.put("calendars", this.calendarToDto(calendar));
            data.put("old",old);
            data.put("update",updated);
            return data;
        }

    }


    public Map<String,Object> deleteCalendar(Authentication authentication, Long id,Long option,LocalDate date){
        User user = this.getUser(authentication);
        Calendar calendar = calendarRepository.findCalendarByUserAndId(user,id).get();
        List<RepeatCalendar> repeatCalendarList;
        List<RepeatCalendarResponseDto> repeatCalendarResponseDtoList = new ArrayList<>();
        repeatCalendarList=repeatCalendarRepository.readRepeatCalendarsByCalendarIdAndIsExpiredIsFalse(calendar);
        Map<String,Object> data = new LinkedHashMap<>();

        if(repeatCalendarList.isEmpty() || repeatCalendarList.size() == 1){
            calendar.setExpired(true);
        }

        //모두 제거
        if(option == 0){
            calendar.setExpired(true);
            for(RepeatCalendar r: repeatCalendarList){
                r.setIsExpired(true);
                repeatCalendarRepository.save(r);
                repeatCalendarResponseDtoList.add(this.repeatCalendarToDto(r));
            }

        }
        //향후 모두 제거
        if (option == 1){
            for(RepeatCalendar r: repeatCalendarList){
                if(r.getDate().isAfter(date)){
                    r.setIsExpired(true);
                    repeatCalendarRepository.save(r);
                    repeatCalendarResponseDtoList.add(this.repeatCalendarToDto(r));
                }
                else {
                    continue;
                }
            }
        }
        //단일 제거
        if (option == 2){
            for(RepeatCalendar r: repeatCalendarList){
                if(r.getDate().isEqual(date)){
                    r.setIsExpired(true);
                    repeatCalendarRepository.save(r);
                    repeatCalendarResponseDtoList.add(this.repeatCalendarToDto(r));
                }
                else{
                    continue;
                }
            }
        }

        calendarRepository.save(calendar);
        data.put("calendars",this.calendarToDto(calendar));
        data.put("repeats", repeatCalendarResponseDtoList);
        return data;

    }


    public List<Calendar> readCalendarsByDate(List<Calendar> calendarList, LocalDate date){
        List<Calendar> calendars = calendarList.stream()
                .filter(calendar -> calendar.getStartDate().isBefore(date)&&calendar.getEndDate().isAfter(date))
                .collect(Collectors.toList());
        return calendars;
    }



    private List<RepeatCalendar> readRepeatCalendars(List<Calendar> calendarList){
        List<Calendar> repeatsInfo = calendarList.stream()
                .filter(calendar -> calendar.getRepeat()!='N')
                .collect(Collectors.toList());
        if(repeatsInfo.isEmpty()){
            return new ArrayList<>();
        }
        List<RepeatCalendar> repeats = new ArrayList<>();
        for (Calendar calendar:repeatsInfo) {
            repeats.addAll(repeatCalendarRepository.readRepeatCalendarsByCalendarIdAndIsExpiredIsFalse(calendar));
        }
        return repeats;
    }

    private User getUser(Authentication authentication) throws NoSuchElementException {
        try{
            Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
            return userOptional.get();
        }catch (RuntimeException e){
            throw new NoSuchElementException();
        }
    }

    private RepeatCalendarResponseDto repeatCalendarToDto(RepeatCalendar repeatCalendar){
        return RepeatCalendarResponseDto.builder()
                .calendarId(repeatCalendar.getCalendarId().getId())
                .date(repeatCalendar.getDate())
                .isExpired(repeatCalendar.getIsExpired())
                .build();
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
