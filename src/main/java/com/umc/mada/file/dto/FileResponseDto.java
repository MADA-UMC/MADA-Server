package com.umc.mada.file.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileResponseDto {
    private int id; // PK
    private String name; // 파일 이름
}