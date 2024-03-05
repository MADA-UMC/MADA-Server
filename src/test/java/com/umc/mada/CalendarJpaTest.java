package com.umc.mada;

import com.umc.mada.calendar.domain.Calendar;
import com.umc.mada.calendar.repository.CalendarRepository;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class CalendarJpaTest {
    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Calendar calendar1;
    private Calendar calendar2;

    @BeforeEach
    public void setUp(){
        user = userRepository.findUserById(5L).get();
        String randomCode = RandomString.make(15);
        String randomName1 = RandomString.make(6);
        String randomName2 = RandomString.make(7);
        calendar1 = Calendar.builder()
                .user(user)
                .calendarName(randomName1)
                .color("#FDA4B4")
                .startDate(LocalDate.of(2024,2,13))
                .endDate(LocalDate.of(2024,3,13))
                .startTime(LocalTime.of(10,0,0))
                .endTime(LocalTime.of(10,0,0))
                .memo(randomCode)
                .dday('N')
                .build();
        calendar2 = Calendar.builder()
                .user(user)
                .calendarName(randomName2)
                .color("#FDA4B4")
                .startDate(LocalDate.of(2024,5,13))
                .endDate(LocalDate.of(2024,6,13))
                .startTime(LocalTime.of(10,0,0))
                .endTime(LocalTime.of(10,0,0))
                .memo(randomCode)
                .dday('N')
                .build();
        calendarRepository.save(calendar1);
        calendarRepository.save(calendar2);
        System.out.println("Calendar1 name : " + calendar1.getCalendarName());
        System.out.println("Calendar2 name : " + calendar1.getCalendarName());
    }

    @DisplayName("달별 조회 테스트 1")
    @Test
    @Order(1)
    public void LoadByMonthTest1(){
        List<Calendar> calendars = calendarRepository.findCalendarMonth(user,2024,2);
        for (Calendar c: calendars) {
            System.out.println("List Calendar Objects name : "+ c.getCalendarName());
        }

        assert(calendars.contains(calendar1));
        assert(!calendars.contains(calendar2));
    }
    @DisplayName("달별 조회 테스트 2")
    @Test
    @Order(2)
    public void LoadByMonthTest2(){
        List<Calendar> calendars = calendarRepository.findCalendarMonth(user,2024,5);

        for (Calendar c: calendars) {
            System.out.println("List Calendar Objects name : "+ c.getCalendarName());
        }

        assert(!calendars.contains(calendar1));
        assert(calendars.contains(calendar2));
    }
    @DisplayName("달별 조회 테스트 3")
    @Test
    @Order(3)
    public void LoadByMonthTest3(){
        List<Calendar> calendars = calendarRepository.findCalendarMonth(user,2024,10);

        for (Calendar c: calendars) {
            System.out.println("List Calendar Objects name : "+ c.getCalendarName());
        }

        assert(calendars.isEmpty());
    }


}
