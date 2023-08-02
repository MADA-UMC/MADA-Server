package com.umc.mada.user.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "USER")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authid;
    private String nickname;
    private String email;
    private String subscribe;
    private String provider;
    private Role role;
    private boolean is_alarm;
    private String refreshToken;

    @Builder
    public void User(String nickname, String email, String subscribe, String provider, Role role, boolean is_alarm){
        this.nickname = nickname;
        this.email = email;
        this.subscribe = subscribe;
        this.provider = provider;
        this.role = role;
        this.is_alarm = is_alarm;
    }

    public void update(String nickname){
        this.nickname=nickname;
    }
}

