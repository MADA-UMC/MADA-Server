package com.umc.mada.user.controller;

import com.umc.mada.user.domain.User;
import com.umc.mada.user.dto.nickname.NicknameRequestDto;
import com.umc.mada.user.repository.UserRepository;
import com.umc.mada.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.umc.mada.todo.repository.TodoRepository;
import com.umc.mada.timetable.repository.TimetableRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/user") //user
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final TimetableRepository timetableRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, TodoRepository todoRepository, TimetableRepository timetableRepository){
        this.userService = userService;
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
        this.timetableRepository = timetableRepository;
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(Authentication authentication) { //@AuthenticationPrincipal CusomtUserDetails cusomtUserDetails
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        System.out.println("oAuth2User = " + oAuth2User);
//        authentication.getPrincipal()
//        System.out.println(cusomtUserDetails.getUser());
//        System.out.println(authentication.getName());
//        return ResponseEntity.status(HttpStatus.OK).body(authentication.getName());
        return ResponseEntity.ok().build();
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
//        return ResponseEntity.status(HttpStatus.OK).body("닉네임 입력 성공했습니다.");
        return ResponseEntity.ok().build();
    }

    @Operation(description = "로그아웃")
    @GetMapping("/logout")
    public ResponseEntity<String> logout(){
        //세션 삭제
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @Operation(description = "회원탈퇴")
    @DeleteMapping("/withdrawal")
    public ResponseEntity<String> withdrawal(Authentication authentication){ //@AuthenticationPrincipal CusomtUserDetails cusomtUserDetails
//        User user = cusomtUserDetails.getUser();
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        userService.withdrawal(userOptional.get());
        return ResponseEntity.ok().build();
    }

//    private User findUser(Authentication authentication){
//        return userRepository.findByAuthId(authentication.getName())
//                .orElseThrow()
//    }

    /**
     * 닉네임 변경 API
     */
    @PatchMapping("/change/nickname")
    public ResponseEntity<Map<String, Object>>changeNickname(Authentication authentication,
                                                                  @Validated @RequestBody NicknameRequestDto changeNicknameRequestDto) {
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        Map<String, Object> map = new HashMap<>();
        map.put("data", userService.changeNickname(user, changeNicknameRequestDto));
//        if(bindingResult.hasErrors()){
//            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
//            return ResponseEntity
//        }
        return ResponseEntity.ok(map);
    }
    @PatchMapping("/subscribe")
    public ResponseEntity<Map<String,Object>> userSubscribe(Authentication authentication,@RequestBody boolean is_subscribe){
        Map<String,Object> map = new HashMap<>();
        map.put("data",new HashMap<>().put("is_subscribe",userService.userSubscribeSettings(authentication,is_subscribe)));
        return ResponseEntity.ok(map);
    }
    @PostMapping("/pageInfo")
    public ResponseEntity<Map<String,Object>> userPageInfo(Authentication authentication, @RequestBody Map<String,Boolean> map){
        return ResponseEntity.ok(userService.userPageSettings(authentication,map));
    }

//    @PatchMapping("/isAlarm")
//    public BaseResponse<>
}
