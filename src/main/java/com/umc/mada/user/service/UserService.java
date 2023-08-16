package com.umc.mada.user.service;

import com.umc.mada.user.domain.User;
import com.umc.mada.user.dto.nickname.NicknameRequestDto;
import com.umc.mada.user.dto.nickname.NicknameResponseDto;
import com.umc.mada.user.dto.user.UserRequestDto;
import com.umc.mada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User update(Long id, UserRequestDto.UpdateNickname request){
        User user = userRepository.findById(id).get();
        user.updateNickname(request.getNickname());
        userRepository.save(user);

        return user;
    }

    public NicknameResponseDto changeNickname(User userAccount, NicknameRequestDto changeNicknameRequestDto) {
        User user = userRepository.findById(userAccount.getId()).get();
        user.updateNickname(changeNicknameRequestDto.getNickname());
        userRepository.save(user);
        return NicknameResponseDto.of(changeNicknameRequestDto.getNickname());
    }
    public boolean userSubscribeSettings(Authentication authentication, boolean is_subscribe){
        User user = this.getUser(authentication);
        user.setSubscribe(is_subscribe);
        return user.isSubscribe();
    }
    public Map<String,Object> userPageSettings(Authentication authentication, Map<String,Boolean> map){
        User user = this.getUser(authentication);
        user.setEndTodoBackSetting(map.get("setEndTodoBackSetting"));
        user.setNewTodoStartSetting(map.get("setNewTodoStartSetting"));
        user.setStartTodoAtMonday(map.get("setStartTodoAtMonday"));
        Map<String,Object> userPageInfos = new HashMap<>();
        userPageInfos.put("setEndTodoBackSetting",user.isEndTodoBackSetting());
        userPageInfos.put("setNewTodoStartSetting",user.isNewTodoStartSetting());
        userPageInfos.put("setStartTodoAtMonday",user.isStartTodoAtMonday());
        return userPageInfos;
    }

    public void nickNameSetting(String nickname, User user){
        userRepository.save(user.setNickname(nickname));
    }

    public void withdrawal(User user){
        userRepository.save(user.expiredUserUpdate());
    }
    private User getUser(Authentication authentication){
        Optional<User> optionalUser = userRepository.findByAuthId(authentication.getName());
        return optionalUser.get();
    }
}
