package com.umc.mada.custom.controller;

import com.umc.mada.custom.service.CustomService;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/api/custom")
public class CustomController {

    private CustomService customService;
    private UserRepository userRepository;

    @Autowired
    public CustomController(CustomService customService, UserRepository userRepository){
        this.customService = customService;
        this.userRepository = userRepository;
    }

    @Operation(description = "사용자 캐릭터 아이템 변경")
    @PatchMapping("/change/{item_id}")
    public ResponseEntity<Void> changeCharacter(Authentication authentication, @PathVariable Long item_id){
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        customService.changeUserItem(user, item_id);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "캐릭터 초기화")
    @GetMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetCharacter(Authentication authentication){
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        customService.resetCharcter(user);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("status", 200);
        result.put("message", "캐릭터 초기화 성공했습니다.");

        return ResponseEntity.ok().body(result);
    }
}
