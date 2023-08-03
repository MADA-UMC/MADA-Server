package com.umc.mada.user.controller;

import com.umc.mada.user.domain.CusomtUserDetails;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.dto.NicknameRequestDto;
import com.umc.mada.user.dto.NicknameResponseDto;
import com.umc.mada.user.dto.UserRequestDto;
import com.umc.mada.global.BaseResponse;
import com.umc.mada.user.repository.UserRepository;
import com.umc.mada.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@RestController
@RequestMapping("/login") //user
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository){
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/test")
    public void test(@AuthenticationPrincipal CusomtUserDetails cusomtUserDetails) {
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        System.out.println("oAuth2User = " + oAuth2User);
//        authentication.getPrincipal()
//        System.out.println(authentication.getPrincipal());
        System.out.println(cusomtUserDetails.getUser());
    }

    @GetMapping("/oauth2/code/{provider}")
    public ResponseEntity<String> login(@PathVariable String provider, HttpServletResponse response){
        //response.getHeader(HttpHeaders.AUTHORIZATION)
        System.out.println(response.getHeader(HttpHeaders.AUTHORIZATION));
        return ResponseEntity.status(HttpStatus.OK).body(response.getHeader(HttpHeaders.AUTHORIZATION));
    }

//    @Operation(description = "회원탈퇴")
//    @DeleteMapping("")
//    public ResponseEntity<Long> withdrawal(Authentication authentication){
//
//    }

//    private User findUser(Authentication authentication){
//        return userRepository.findByAuthId(authentication.getName())
//                .orElseThrow()
//    }

    /**
     * 닉네임 변경 API
     */
    @PatchMapping("/change/nickname")
    public BaseResponse<NicknameResponseDto> changeNickname(Authentication authentication,
                                                            @Validated @RequestBody NicknameRequestDto changeNicknameRequestDto, BindingResult bindingResult) {
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        if(bindingResult.hasErrors()){
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            return BaseResponse.onFailure(400, objectError.getDefaultMessage(), null);
        }

        NicknameResponseDto result = userService.changeNickname(user, changeNicknameRequestDto);
        return BaseResponse.onSuccess(result);
    }

}
