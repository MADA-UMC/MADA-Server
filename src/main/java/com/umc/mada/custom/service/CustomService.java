package com.umc.mada.custom.service;

import com.umc.mada.custom.repository.CustomRepository;
import com.umc.mada.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomService {

    private final CustomRepository customRepository;

    public void changeUserItem(User user, Integer item_id){

    }

}
