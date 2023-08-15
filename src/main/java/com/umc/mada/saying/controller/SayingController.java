package com.umc.mada.saying.controller;

import com.umc.mada.saying.dto.SayingResponseDto;
import com.umc.mada.saying.service.SayingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SayingController {
    private final SayingService sayingService;
    @GetMapping("/saying")
    public List<SayingResponseDto> getAllSaying() { return sayingService.findAllSaying(); }
}
