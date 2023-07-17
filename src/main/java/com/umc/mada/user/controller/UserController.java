package com.umc.mada.user.controller;

import com.umc.mada.user.domain.User;
import com.umc.mada.user.dto.UserRequestDto;
import com.umc.mada.global.BaseResponse;
import com.umc.mada.user.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PatchMapping("/{userId}/nickname")
    public BaseResponse<String> UpdateNickname(@PathVariable(name = "userId") Long userId, @RequestBody UserRequestDto.UpdateNickname request) {
        User user = userService.update(userId, request);
        return new BaseResponse<>("닉네임 수정 완료");
    }
}
