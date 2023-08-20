package com.umc.mada.saying.controller;

import com.umc.mada.saying.dto.SayingResponseDto;
import com.umc.mada.saying.service.SayingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class SayingController {
    private final SayingService sayingService;
    @GetMapping("/my")
    public List<SayingResponseDto> getAllSaying() { return sayingService.findAllSaying(); }

//    @GetMapping("/mys")
//    public ResponseEntity<Map<String, Object>>myInfo(Authentication authentication) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("data", sayingService.findAllSaying(authentication));
//        return ResponseEntity.ok(map);
//    }
}
