package com.umc.mada.my.dto;

import com.umc.mada.my.domain.My;
import lombok.*;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyResponseDto {
    private Long id;
    private String content;
    private String sayer;

    public MyResponseDto(My my) {
        this.id = my.getId();
        this.content = my.getContent();
        this.sayer = my.getSayer();
    }

    public MyResponseDto(Optional<My> my) {
        this.id = my.get().getId();
        this.content = my.get().getContent();
        this.sayer = my.get().getSayer();
    }
}
