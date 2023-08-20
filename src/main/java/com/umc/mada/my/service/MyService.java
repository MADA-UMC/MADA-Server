package com.umc.mada.my.service;

import com.umc.mada.my.domain.My;
import com.umc.mada.my.dto.MyResponseDto;
import com.umc.mada.my.repository.MyRepository;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.service.UserService;
import com.umc.mada.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MyService {
    private final MyRepository myRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<MyResponseDto> findRandomSaying() {
        try {
            List<My> myList = myRepository.findAllDesc();
            List<MyResponseDto> responseDtoList = new ArrayList<>();
            for (My my : myList) {
                responseDtoList.add(
                        new MyResponseDto(my)
                );
            }
            return responseDtoList;
        } catch (Exception e) {
        }
        return null;
    }

    private User getUser(Authentication authentication){
        Optional<User> optionalUser = userRepository.findByAuthId(authentication.getName());
        return optionalUser.get();
    }

    public Map<String, Object> myProfile(Authentication authentication) {
        User user = getUser(authentication);
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("nickname", user.getNickname());
        userProfile.put("saying", this.findRandomSaying());
        userProfile.put("subscribe", user.getSubscribe());
        return userProfile;
    }

}
