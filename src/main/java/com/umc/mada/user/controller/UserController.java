package com.umc.mada.user.controller;

import com.umc.mada.user.domain.User;
import com.umc.mada.user.dto.UserRequestDto;
import com.umc.mada.global.BaseResponse;
import com.umc.mada.user.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

//    @PatchMapping("/{id}/nickname")
//    public BaseResponse<String> UpdateNickname(@PathVariable(name = "id") Long id, @RequestBody UserRequestDto.UpdateNickname request) {
//        User user = userService.update(id, request);
//        return new BaseResponse<>("닉네임 수정 완료");
//    }
}
