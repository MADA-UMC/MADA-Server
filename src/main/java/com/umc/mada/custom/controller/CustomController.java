package com.umc.mada.custom.controller;

import com.umc.mada.custom.service.CustomService;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/custom")
public class CustomController {

    private CustomService customService;
    private UserRepository userRepository;

    @Operation(description = "사용자 캐릭터 아이템 변경")
    @PatchMapping("/change/{item_id}")
    public ResponseEntity<Void> change(Authentication authentication, Integer item_id){
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();

        return ResponseEntity.ok().build();
    }

}
