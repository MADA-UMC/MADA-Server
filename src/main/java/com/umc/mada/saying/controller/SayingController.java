package com.umc.mada.saying.controller;

import com.umc.mada.saying.dto.SayingResponseDto;
import com.umc.mada.saying.service.SayingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class SayingController {
    private final SayingService sayingService;
    @GetMapping("/my")
    public List<SayingResponseDto> getAllSaying() { return sayingService.findAllSaying(); }
}
