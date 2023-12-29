package com.umc.mada.calendar.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder

@Table(name = "REPEAT_CALENDAR")
public class RepeatCalendar {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    @JoinColumn(name="calendar_id")
    @ManyToOne
    private Calendar calendarId;

    @Column(name = "`date`")
    private LocalDate date;
    //No, Day, Week, Month, Year

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt; // 생성 시간

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt; // 수정 시간
}
