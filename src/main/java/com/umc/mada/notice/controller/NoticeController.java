package com.umc.mada.notice.controller;

import com.umc.mada.notice.dto.NoticeListResponseDto;
import com.umc.mada.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    private  final NoticeService noticeService;
    @GetMapping("/notices")
    public List<NoticeListResponseDto> getAllNotice() { return noticeService.findAllNotice(); }
}
