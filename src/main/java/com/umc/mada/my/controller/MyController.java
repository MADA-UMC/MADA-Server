package com.umc.mada.my.controller;

import com.umc.mada.my.dto.MyResponseDto;
import com.umc.mada.my.service.MyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class MyController {
    private final MyService myService;
//    @GetMapping("/my")
//    public List<MyResponseDto> getAllSaying() { return myService.findAllSaying(); }

    @GetMapping("/my")
    public ResponseEntity<Map<String, Object>> myInfo(Authentication authentication) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", myService.myProfile(authentication));
        return ResponseEntity.ok(map);
    }
}
