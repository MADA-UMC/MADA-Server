package com.umc.mada.user.domain;

import javax.persistence.*;

//import com.umc.mada.BaseTimeEntity;
import com.umc.mada.custom.domain.HaveItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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
    private boolean subscribe;
    private String provider;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Column(name = "start_todo_at_monday") //TODO: Cannot resolve column 'start_todo_at_monday'
    private boolean startTodoAtMonday;
    @Column(name = "end_todo_back_setting") //TODO: Cannot resolve column
    private boolean endTodoBackSetting;
    @Column(name = "new_todo_start_setting") //TODO: Cannot resolve column
    private boolean newTodoStartSetting;
    private boolean account_expired;
    @Column(nullable = false, name = "is_alarm") //TODO: Cannot resolve column
    private boolean isAlarm;
    private String refreshToken;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false) //TODO: Cannot resolve column
    private LocalDateTime createdAt; // 생성 시간

    @UpdateTimestamp
    @Column(name = "update_at") //TODO: Cannot resolve column
    private LocalDateTime updatedAt; // 수정 시간

    @OneToMany(mappedBy = "user")
    private List<HaveItem> haveItems = new ArrayList<>();

    @Builder
    public User(String authId, String nickname, String email, boolean subscribe, String provider, Role role, boolean isAlarm) {
        this.authId = authId;
        this.nickname = nickname;
        this.email = email;
        this.subscribe = subscribe;
        this.provider = provider;
        this.role = role;
        this.isAlarm = isAlarm;
    }

    public void updateNickname(String nickname) { this.nickname = nickname; }

    public User update(String authId, String email) {
        this.authId = authId;
        this.email = email;
        return this;
    }

    public User expiredUserUpdate() {
        this.account_expired = true;
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
    public User updatePageSetting(boolean startTodoAtMonday, boolean endTodoBackSetting,boolean newTodoStartSetting){
        this.startTodoAtMonday = startTodoAtMonday;
        this.endTodoBackSetting = endTodoBackSetting;
        this.newTodoStartSetting = newTodoStartSetting;
        return this;
    }
}

