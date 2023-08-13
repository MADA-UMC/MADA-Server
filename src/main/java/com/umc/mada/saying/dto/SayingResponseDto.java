package com.umc.mada.saying.dto;

import com.umc.mada.saying.domain.Saying;
import lombok.*;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SayingResponseDto {
    private Long id;
    private String content;
    private String sayer;

    public SayingResponseDto(Saying saying) {
        this.id= saying.getId();
        this.content = saying.getContent();
        this.sayer = saying.getSayer();
    }

    public SayingResponseDto(Optional<Saying> saying) {
        this.id = saying.get().getId();
        this.content = saying.get().getContent();
        this.sayer = saying.get().getSayer();
    }
}
