package com.umc.mada.auth.service;

import com.umc.mada.auth.dto.OAuth2Attributes;
import com.umc.mada.user.domain.CusomtUserDetails;
import com.umc.mada.user.domain.Role;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.AuthProvider;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService extends DefaultOAuth2UserService{ // implements OAuth2UserService<OAuth2UserRequest, OAuth2User>
    private final UserRepository userRepository;

    //userRequest로
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
//        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        return process(userRequest, oAuth2User);
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User){
        //진행중인 서비스를 구분 ex) google, naver, kakao
        String provider = userRequest.getClientRegistration().getRegistrationId();
        //OAuth2 로그인 시 키 값이 되는 부분으로 각 서비스마다 다르기 때문에 변수로 받아서 넣어줘야한다.
        // 구글은 "sub", 네이버는 response, 카카오는 id이다.
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(provider, userNameAttributeName, oAuth2User.getAttributes());

        Optional<User> userOptional = userRepository.findByAuthId(oAuth2Attributes.getAuthId());

        User user;
        //가입되어 있는 경우
        if(userOptional.isPresent()){
            user = userOptional.get();
            //유저 정보 업데이트
            user = user.update(oAuth2Attributes.getAuthId(), oAuth2Attributes.getEmail());
        } else{
            //첫 로그인인 경우 사용자를 회원가입(등록)한다.
            user = createUser(oAuth2Attributes, provider);
        }
//
//        return new DefaultOAuth2User(Collections.singleton(
//                new SimpleGrantedAuthority(user.getRole().getKey())),oAuth2Attributes.getAttributes(), oAuth2Attributes.getNameAttributeKey());
        return CusomtUserDetails.create(user, oAuth2Attributes.getAttributes());
    }

    private User createUser(OAuth2Attributes oAuth2Attributes, String authProvider){
        User user = User.builder()
                .authId(oAuth2Attributes.getAuthId())
                .nickname(oAuth2Attributes.getName())
                .email(oAuth2Attributes.getEmail())
                .role(Role.USER)
                .provider(authProvider)
                .build();
        return userRepository.save(user);
    }

}
