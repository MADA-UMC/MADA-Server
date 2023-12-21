package com.umc.mada.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    @NotNull
    private String message;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

    @NotNull
    private String url;

    public ErrorResponse(final String message){
        this.message = message;
        timestamp = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }


}
