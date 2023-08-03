package com.umc.mada.user.service;

import com.umc.mada.user.domain.User;
import com.umc.mada.user.dto.NicknameRequestDto;
import com.umc.mada.user.dto.NicknameResponseDto;
import com.umc.mada.user.dto.UserRequestDto;
import com.umc.mada.user.dto.UserResponseDto;
import com.umc.mada.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
