package com.umc.mada.user.service;

import com.umc.mada.custom.domain.CustomItem;
import com.umc.mada.custom.domain.HaveItem;
import com.umc.mada.custom.repository.CustomRepository;
import com.umc.mada.custom.repository.HaveItemRepository;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.dto.nickname.NicknameRequestDto;
import com.umc.mada.user.dto.nickname.NicknameResponseDto;
import com.umc.mada.user.dto.user.UserRequestDto;
import com.umc.mada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CustomRepository customRepository;
    private final HaveItemRepository haveItemRepository;
    @Autowired
    public UserService(UserRepository userRepository,CustomRepository customRepository, HaveItemRepository haveItemRepository){
        this.haveItemRepository = haveItemRepository;
        this.userRepository = userRepository;
        this.customRepository = customRepository;
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
    @Transactional
    public Boolean isSubscribe(Authentication authentication, Map<String,Boolean> is_subscribe) {
        boolean condition = true;
        User user = this.getUser(authentication);
        if(user.getSubscribe() != is_subscribe.get("is_subscribe"))
            condition = false;
        user.updateSubscribe(is_subscribe.get("is_subscribe"));
        userRepository.save(user);
        if(!condition){
            if(user.getSubscribe()){
                List<CustomItem> subscribeCustomItems = customRepository.findByUnlockCondition(CustomItem.ItemUnlockCondition.PREMINUM);
                for (CustomItem customItem : subscribeCustomItems){
                    haveItemRepository.save(HaveItem.builder().user(user).customItem(customItem).build());
                }
            }
            else{
                haveItemRepository.deleteByUserAndUnlockCond(user, CustomItem.ItemUnlockCondition.PREMINUM);
            }
        }


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
        Map<String, Object> alarmSet = new HashMap<>();
        alarmSet.put("calendarAlarmSetting", user.isCalendarAlarmSetting());
        alarmSet.put("dDayAlarmSetting", user.isDDayAlarmSetting());
        alarmSet.put("timetableAlarmSetting", user.isTimetableAlarmSetting());
        return alarmSet;
    }

    public void setNickname (Map<String, String> nickname, User user) {
        userRepository.save(user.setNickname(nickname.get("nickname")));
    }

    public int calcAttendance(Authentication authentication) {
        User user = this.getUser(authentication);
        int attendanceCount = user.getAttendanceCount() + 1;
        user.setAttendanceCount(attendanceCount);
        userRepository.save(user);
        return attendanceCount;
    }

    public int findAttendanceCount(Authentication authentication) {
        User user = this.getUser(authentication);
        int totalAttendanceCount = user.getAttendanceCount();
        return totalAttendanceCount;
    }

    public void removeUser(User user){
        userRepository.save(user.expiredUserUpdate());
    }

    private User getUser(Authentication authentication){
        User user = userRepository.findByAuthIdAndAccountExpired(authentication.getName(), false).orElseThrow(()-> new RuntimeException("올바른 유저 ID가 아닙니다."));
        return user;
    }
}
