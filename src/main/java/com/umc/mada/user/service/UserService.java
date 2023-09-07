package com.umc.mada.user.service;

import com.umc.mada.user.domain.User;
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

    public Map<String, String> findUserProfile(Authentication authentication) {
        User user = getUser(authentication);
        Map<String, String> userProfile = new HashMap<>();
        userProfile.put("nickname", user.getNickname());
        userProfile.put("email", user.getEmail());
        userRepository.save(user);
        return userProfile;
    }

    public NicknameResponseDto modifyNickname(User userAccount, NicknameRequestDto changeNicknameRequestDto) {
        User user = userRepository.findById(userAccount.getId()).get();
        user.updateNickname(changeNicknameRequestDto.getNickname());
        userRepository.save(user);
        return NicknameResponseDto.of(changeNicknameRequestDto.getNickname());
    }

    public Boolean isSubscribe(Authentication authentication, Map<String,Boolean> is_subscribe) {
        User user = this.getUser(authentication);
        user.updateSubscribe(is_subscribe.get("is_subscribe"));
        userRepository.save(user);
        return user.getSubscribe();
    }

    public Map<String, Object> saveUserPageSet(Authentication authentication, Map<String, Boolean> map) {
        User user = this.getUser(authentication);
        user.updatePageSetting(map.get("endTodoBackSetting"), map.get("newTodoStartSetting"), map.get("startTodoAtMonday"));
        Map<String, Object> userPageInfos = new HashMap<>();
        userPageInfos.put("endTodoBackSetting",user.isEndTodoBackSetting());
        userPageInfos.put("newTodoStartSetting",user.isNewTodoStartSetting());
        userPageInfos.put("startTodoAtMonday",user.isStartTodoAtMonday());
        userRepository.save(user);
        return userPageInfos;
    }

    public Map<String, Object> saveUserAlarmSet(Authentication authentication, Map<String, Boolean>map) {
        User user = this.getUser(authentication);
        user.updateAlarmSetting(map.get("calendarAlarmSetting"), map.get("dDayAlarmSetting"), map.get("timetableAlarmSetting"));
        Map<String, Object> userAlarmInfos = new HashMap<>();
        userAlarmInfos.put("calendarAlarmSetting", user.isCalendarAlarmSetting());
        userAlarmInfos.put("dDayAlarmSetting", user.isDDayAlarmSetting());
        userAlarmInfos.put("timetableAlarmSetting", user.isTimetableAlarmSetting());
        userRepository.save(user);
        return userAlarmInfos;
    }

    public Map<String, Object> findUserPageSet(Authentication authentication) {
        User user = this.getUser(authentication);
        Map<String, Object> pageSet = new HashMap<>();
        pageSet.put("endTodoBackSetting", user.isEndTodoBackSetting());
        pageSet.put("startTodoAtMonday", user.isStartTodoAtMonday());
        pageSet.put("newTodoStartSetting", user.isNewTodoStartSetting());
        return pageSet;
    }

    public Map<String, Object> findUserAlarmSet(Authentication authentication) {
        User user = this.getUser(authentication);
        Map<String, Object> pageSet = new HashMap<>();
        pageSet.put("calendarAlarmSetting", user.isCalendarAlarmSetting());
        pageSet.put("dDayAlarmSetting", user.isDDayAlarmSetting());
        pageSet.put("timetableAlarmSetting", user.isTimetableAlarmSetting());
        return pageSet;
    }

    public void nickNameSetting(Map<String, String> nickName, User user) {
        userRepository.save(user.setNickname(nickName.get("nickName")));
    }

    public void withdrawal(User user){
        userRepository.save(user.expiredUserUpdate());
    }

    private User getUser(Authentication authentication){
        Optional<User> optionalUser = userRepository.findByAuthId(authentication.getName());
        return optionalUser.get();
    }
}
