package com.umc.mada.notice.dto;

import com.umc.mada.notice.domain.Notice;

import lombok.*;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NoticeListResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDate date;

    public NoticeListResponseDto(Notice notice) {
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.id = notice.getId();
        this.date = notice.getDate();
    }

    public NoticeListResponseDto(Optional<Notice> notice) {
        this.title = notice.get().getTitle();
        this.content = notice.get().getContent();
        this.id = notice.get().getId();
        this.date = notice.get().getDate();
    }
}
