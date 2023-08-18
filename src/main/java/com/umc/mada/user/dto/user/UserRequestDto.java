package com.umc.mada.user.dto.user;

import lombok.*;

public class UserRequestDto {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UpdateNickname {
        private String nickname;
    }
}
