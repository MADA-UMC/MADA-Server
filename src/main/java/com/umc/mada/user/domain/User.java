package com.umc.mada.user.domain;

import javax.persistence.*;

import com.umc.mada.custom.domain.HaveItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String authId;
    private String nickname;
    @Column(unique = true, nullable = false)
    private String email;
    private Boolean subscribe;
    private String provider;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    // Attendance 출석
    @Column(name = "attendance_count", nullable = false)
    private int attendanceCount;

    @Column(name = "start_todo_at_monday") //TODO: Cannot resolve column 'start_todo_at_monday'
    private boolean startTodoAtMonday;
    @Column(name = "end_todo_back_setting") //TODO: Cannot resolve column
    private boolean endTodoBackSetting;
    @Column(name = "new_todo_start_setting") //TODO: Cannot resolve column
    private boolean newTodoStartSetting;
    @Column(name="account_expired")
    private boolean accountExpired; //회원탈퇴 여부
    private String refreshToken;
    @Column(name = "google_access_token")
    private String googleAccessToken; //TODO: 테이블 분리하기
    @Column(nullable = false, name = "is_alarm") //TODO: Cannot resolve column
    private boolean isAlarm;
    @Column(name = "calendar_alarm_setting") //TODO: Cannot resolve column
    private boolean calendarAlarmSetting;
    @Column(name = "d_day_alarm_setting") //TODO: Cannot resolve column
    private boolean dDayAlarmSetting;
    @Column(name = "timetable_alarm_setting") //TODO: Cannot resolve column
    private boolean timetableAlarmSetting;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false) //TODO: Cannot resolve column
    private LocalDateTime createdAt; // 생성 시간

    @UpdateTimestamp
    @Column(name = "update_at") //TODO: Cannot resolve column
    private LocalDateTime updatedAt; // 수정 시간

    @OneToMany(mappedBy = "user")
    private List<HaveItem> haveItems = new ArrayList<>();

    @Builder
    public User(String authId, String nickname, String email, boolean subscribe, String provider, Role role, int attendanceCount , boolean isAlarm, String googleAccessToken) {
        this.authId = authId;
        this.nickname = nickname;
        this.email = email;
        this.subscribe = subscribe;
        this.provider = provider;
        this.role = role;
        this.attendanceCount = attendanceCount;
        this.isAlarm = isAlarm;
        this.googleAccessToken = googleAccessToken;
    }

    public void updateNickname(String nickname) { this.nickname = nickname; }

    public User update(String authId, String email) {
        this.authId = authId;
        this.email = email;
        return this;
    }

    public User update(String authId, String email, String googleAccessToken) {
        this.authId = authId;
        this.email = email;
        this.googleAccessToken = googleAccessToken;
        return this;
    }

    public User expiredUserUpdate() {
        this.accountExpired = true;
        return this;
    }

    public void updateSubscribe(boolean subscribe){
        this.subscribe = subscribe;
    }
    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public void setAttendanceCount(int attendanceCount) {
        this.attendanceCount = attendanceCount;
    }

    public User updatePageSetting(boolean endTodoBackSetting, boolean newTodoStartSetting, boolean startTodoAtMonday) {
        this.startTodoAtMonday = startTodoAtMonday;
        this.endTodoBackSetting = endTodoBackSetting;
        this.newTodoStartSetting = newTodoStartSetting;
        return this;
    }

    public User updateAlarmSetting(boolean calendarAlarmSetting, boolean dDayAlarmSetting, boolean timetableAlarmSetting) {
        this.calendarAlarmSetting = calendarAlarmSetting;
        this.dDayAlarmSetting = dDayAlarmSetting;
        this.timetableAlarmSetting = timetableAlarmSetting;
        return this;
    }

    public User updateProfile(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
        return this;
    }
}

