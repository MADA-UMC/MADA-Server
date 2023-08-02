//package com.umc.mada.calendar.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import com.umc.mada.calendar.domain.Calendar;
//import com.umc.mada.calendar.dto.CalendarRequestDto;
//import com.umc.mada.calendar.repository.CalendarRepository;
//import com.umc.mada.user.repository.UserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.util.List;
//
//@Service
//@Slf4j //로그처리
//@Transactional //트렌젝션 처리
//public class CalendarService {
//    private final CalendarRepository calendarRepository;
//    //의존성주입 (DI)
//    @Autowired
//    public  CalendarService(CalendarRepository calendarRepository){
//        this.calendarRepository = calendarRepository;
//    }
//    //캘린더 생성코드
//    public ResponseEntity<ObjectNode> createCalendar(CalendarRequestDto calenderDto){
//        try{
//            calendarRepository.save(
//                    //builder에서 calender_id()를 비워두어도 CalenderEntity에 IDENTITY타입 덕에 자동 생성 처리됨
//                    Calendar.builder()
//                            //User Entity 부재
//                            //.user_id(userRepository.getReferenceById(calenderDto.getUser_id()))
//                            .calender_name(calenderDto.getCalender_name())
//                            .d_day(calenderDto.getD_day())
//                            .repeat(calenderDto.getRepeat())
//                            .memo(calenderDto.getMemo())
//                            .start_date(calenderDto.getStart_date())
//                            .end_date(calenderDto.getEnd_date())
//                            .build());
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            ObjectNode jsonObject =  objectMapper.createObjectNode();
//            jsonObject.put("status","200");
//            jsonObject.put("message","요청을 성공적으로 처리하였습니다.");
//
//            return ResponseEntity.ok(jsonObject);
//        }catch (Exception e){
//            e.printStackTrace();
//            return ResponseEntity.status(400).build();
//
//        }
//    }
//}
