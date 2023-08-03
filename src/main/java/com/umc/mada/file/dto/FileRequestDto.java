package com.umc.mada.file.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileRequestDto {
    private String name; // 파일 이름
}
