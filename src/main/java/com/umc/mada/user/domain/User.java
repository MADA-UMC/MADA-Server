package com.umc.mada.user.domain;

import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authid;
    private String nickname;
    private String phone;
    private String email;
    private String subscribe;
    private String provider;
    private Role role;

    @Builder
    public void User(String nickname, String phone, String email, String subscribe, String provider, Role role){
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.subscribe = subscribe;
        this.provider = provider;
        this.role = role;
    }

    public void update(String nickname){
        this.nickname=nickname;
    }
}

