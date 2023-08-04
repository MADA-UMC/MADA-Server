package com.umc.mada.user.dto.nickname;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class NicknameResponseDto {
    private final String nickname;
    private NicknameResponseDto(String nickname) {
        this.nickname = nickname;
    }

    public  static NicknameResponseDto of (String nickname) {
        return new NicknameResponseDto(nickname);
    }
}
