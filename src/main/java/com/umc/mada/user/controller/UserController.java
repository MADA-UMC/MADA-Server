package com.umc.mada.user.controller;

import com.umc.mada.user.domain.User;
import com.umc.mada.user.dto.nickname.NicknameRequestDto;
import com.umc.mada.user.dto.nickname.NicknameResponseDto;
import com.umc.mada.global.BaseResponse;
import com.umc.mada.user.repository.UserRepository;
import com.umc.mada.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/user") //user
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository){
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(Authentication authentication) { //@AuthenticationPrincipal CusomtUserDetails cusomtUserDetails
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        System.out.println("oAuth2User = " + oAuth2User);
//        authentication.getPrincipal()
        System.out.println(authentication.getName());
//        System.out.println(cusomtUserDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(authentication.getName());
    }

//    @GetMapping("/oauth2/code/{provider}")
//    public ResponseEntity<String> login(@PathVariable String provider, HttpServletResponse response){
//        //response.getHeader(HttpHeaders.AUTHORIZATION)
//        System.out.println(response.getHeader(HttpHeaders.AUTHORIZATION));
//        return ResponseEntity.status(HttpStatus.OK).body(response.getHeader(HttpHeaders.AUTHORIZATION));
//    }

    @Operation(description = "회원가입한 유저가 닉네임 입력하는 곳")
    @PostMapping("/singup/{nickName}")
    public ResponseEntity<String> singupNickName(@PathVariable String nickName, Authentication authentication){
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        userService.nickNameSetting(nickName, userOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("닉네임 입력 성공했습니다.");
    }

    @Operation(description = "로그아웃")
    @GetMapping("/logout")
    public ResponseEntity<String> logout(){
        //세션 삭제
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.OK).body("로그아웃 성공했습니다.");
    }

    @Operation(description = "회원탈퇴")
    @DeleteMapping("/withdrawal")
    public ResponseEntity<String> withdrawal(Authentication authentication){ //@AuthenticationPrincipal CusomtUserDetails cusomtUserDetails
//        User user = cusomtUserDetails.getUser();
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        userService.withdrawal(userOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("회원탈퇴에 성공했습니다.");
    }

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
