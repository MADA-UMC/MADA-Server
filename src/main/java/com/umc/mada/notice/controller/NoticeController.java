package com.umc.mada.notice.controller;

import com.umc.mada.notice.dto.NoticeListResponseDto;
import com.umc.mada.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    private  final NoticeService noticeService;
    @GetMapping("/allnotice")
    public ResponseEntity<Map<String, Object>> getAllNotice() {
        Map<String, Object> map = new HashMap<>();
        map.put("data", noticeService.findAllNotice());
        return ResponseEntity.ok(map);
    }
}
