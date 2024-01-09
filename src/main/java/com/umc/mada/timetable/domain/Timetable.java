package com.umc.mada.timetable.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.umc.mada.timetable.domain.DayOfWeek;
import com.umc.mada.user.domain.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "TIMETABLE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@AllArgsConstructor
@NoArgsConstructor
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User userId;

    @Column(name = "schedule_name", nullable = false)
    private String scheduleName;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "memo")
    private String memo;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name= "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    // 생성자
    public Timetable(User userId, LocalDate date, String scheduleName, String color, LocalTime startTime, LocalTime endTime, String memo, Boolean isDeleted, DayOfWeek dayOfWeek){
        this.userId = userId;
        this.date = date;
        this.scheduleName = scheduleName;
        this.color = color;
        this.startTime = startTime;
        this.endTime = endTime;
        this.memo = memo;
        this.isDeleted = isDeleted;
        this.dayOfWeek = dayOfWeek;
    }
}
