package com.umc.mada.user.dto;

import lombok.*;

public class UserRequestDto {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class UpdateNickname{
        private String nickname;
    }
}
