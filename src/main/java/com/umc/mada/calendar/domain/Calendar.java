package com.umc.mada.calendar.domain;


import com.umc.mada.user.domain.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CALENDAR")
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "calendar_name")
    private String calendarName;
    @Column(name = "color")
    private String color;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name ="start_time")
    private LocalTime startTime;
    @Column(name = "end_time")
    private LocalTime endTime;
    @Column(name = "`repeat`")
    //No, Day, Week, Month, Year
    private Character  repeat;
    @Column(name = "repeat_info")
    private Integer repeatInfo;
    @Column(name = "d_day")
    private Character dday;
    @Column(name = "memo")
    private String memo;
    @Column(name="is_expired")
    @ColumnDefault("0")
    private boolean isExpired;
    // TODO: 2023-09-01 isExpirde 데이터베이스에 컬럼 추가하기

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt; // 생성 시간

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt; // 수정 시간
}
