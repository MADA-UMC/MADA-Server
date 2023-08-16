package com.umc.mada.user.service;

import com.umc.mada.user.domain.User;
import com.umc.mada.user.dto.alarm.AlarmSetRequestDto;
import com.umc.mada.user.dto.alarm.AlarmSetResponseDto;
import com.umc.mada.user.dto.nickname.NicknameRequestDto;
import com.umc.mada.user.dto.nickname.NicknameResponseDto;
import com.umc.mada.user.dto.user.UserRequestDto;
import com.umc.mada.user.dto.user.UserResponseDto;
import com.umc.mada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

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
    public Boolean userSubscribeSettings(Authentication authentication, Map<String,Boolean> is_subscribe){
        User user = this.getUser(authentication);
        user.updateSubscribe(is_subscribe.get("is_subscribe"));
        userRepository.save(user);
        return user.getSubscribe();
    }
    public Map<String,Object> userPageSettings(Authentication authentication, Map<String,Boolean> map){
        User user = this.getUser(authentication);
        user.updatePageSetting(map.get("endTodoBackSetting"),map.get("newTodoStartSetting"),map.get("startTodoAtMonday"));
        Map<String,Object> userPageInfos = new HashMap<>();
        userPageInfos.put("endTodoBackSetting",user.isEndTodoBackSetting());
        userPageInfos.put("newTodoStartSetting",user.isNewTodoStartSetting());
        userPageInfos.put("startTodoAtMonday",user.isStartTodoAtMonday());
        userRepository.save(user);
        return userPageInfos;
    }

    public void nickNameSetting(String nickName, User user) {
        userRepository.save(user.setNickname(nickName));
    }
    public void withdrawal(User user){
        userRepository.save(user.expiredUserUpdate());
    }

    private User getUser(Authentication authentication){
        Optional<User> optionalUser = userRepository.findByAuthId(authentication.getName());
        return optionalUser.get();
    }

//    @Override
//    @Transactional
//    public AlarmSetResponseDto updateAlarm(Long id, AlarmSetRequestDto alarmSetRequestDto) {
//
//    }
//    @Transactional
//    public UserResponseDto toggleAlarm(User userAccount) {
//        User user = userRepository.findUserById(userAccount.getId()).get();
//        user.updateAlarm(!user.getIsAlarm());
//        userRepository.save(user);
//        return UserResponseDto.of()
//    }
}
