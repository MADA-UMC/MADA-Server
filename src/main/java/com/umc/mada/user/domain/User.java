package com.umc.mada.user.domain;

import javax.persistence.*;

//import com.umc.mada.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER")
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

    private boolean account_expired;
    private boolean is_alarm;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt; // 생성 시간

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt; //

    @Builder
    public User(String authId, String nickname, String email, boolean subscribe, String provider, Role role){
        this.authId = authId;
        this.nickname = nickname;
        this.email = email;
        this.subscribe = subscribe;
        this.provider = provider;
        this.role = role;
    }

    public void updateNickname(String nickname){
        this.nickname=nickname;
    }

    public User update(String authId, String email){
        this.authId = authId;
        this.email = email;
        return this;
    }

    public User expiredUser(){
        this.account_expired = true;
        return this;
    }
}

