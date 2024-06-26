package com.umc.mada.custom.controller;

import com.umc.mada.custom.dto.CustomItemsResponse;
import com.umc.mada.custom.dto.UserCharacterResponse;
import com.umc.mada.custom.service.CustomService;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
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

    @Operation(description = "사용자 캐릭터 출력")
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> printUserCharacter(Authentication authentication){
        User user = findUser(authentication);
        UserCharacterResponse userCharacterResponse = customService.printUserCharacter(user);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", userCharacterResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "아이템 목록 반환(출석미션 아이템의 경우는 소유하고 있는 아이템만 포함한다)")
    @GetMapping("/item")
    public ResponseEntity<Map<String, Object>> getItemList(Authentication authentication){
        User user = findUser(authentication);
        CustomItemsResponse customItemsResponse = customService.getItemList(user);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", customItemsResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "현재 화면의 아이템 타입에 맞는 아이템 조회하기/ 해당 타입의 아이템 목록, 사용자의 소유 여부 반환")
    @GetMapping("/item/{item_type}")
    public ResponseEntity<Map<String, Object>> findItemsByItemType(@PathVariable String item_type, Authentication authentication){
        User user = findUser(authentication);
        CustomItemsResponse customItemsResponse = customService.findItemsByType(user, item_type);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", customItemsResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "사용자 캐릭터 착용 아이템 변경")
    @PatchMapping("/change") ///{item_id}
    public ResponseEntity<Map<String, Object>> changeCharacter(Authentication authentication, @RequestParam(value="item_id") List<Integer> items_id){ // @RequestParam(value="items_id[]") List<String> items_id
        User user = findUser(authentication);
        //String[] items_id = request.getParameterValues("item_id");
        UserCharacterResponse userCharacterResponse = customService.changeUserItem(user, items_id);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("data", userCharacterResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "캐릭터 초기화")
    @GetMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetCharacter(Authentication authentication){
        User user = findUser(authentication);
        UserCharacterResponse userCharacterResponse = customService.resetCharcter(user);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("data", userCharacterResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "아이템 구매")
    @PostMapping("/buy/{item_id}")
    public ResponseEntity<Void> buyItem(Authentication authentication, @PathVariable int item_id){
        User user = findUser(authentication);
        customService.buyItem(user, item_id);
        return ResponseEntity.ok().build();
    }


    private User findUser(Authentication authentication){
        User user = userRepository.findByAuthIdAndAccountExpired(authentication.getName(), false).orElseThrow(()-> new RuntimeException("올바른 유저 ID가 아닙니다."));
//        User user = userOptional.get(); //TODO: get값이 NULL인 경우를 체크해줘야함
        return user;
    }
}
