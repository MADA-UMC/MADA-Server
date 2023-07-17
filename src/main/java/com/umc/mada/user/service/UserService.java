package com.umc.mada.user.service;

import com.umc.mada.user.domain.User;
import com.umc.mada.user.dto.UserRequestDto;
import com.umc.mada.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User update(Long userId, UserRequestDto.UpdateNickname request){
        User user = userRepository.findById(userId).get();
        user.update(request.getNickname());
        userRepository.save(user);

        return user;
    }
}
