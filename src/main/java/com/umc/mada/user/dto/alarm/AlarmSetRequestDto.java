package com.umc.mada.user.dto.alarm;

import lombok.*;

@Builder
@NoArgsConstructor
//@AllArgsConstructor
@Getter
public class AlarmSetRequestDto {
    public static class AlarmUpdateRequestDto {
        private Long id;
        private boolean set;
    }
}
