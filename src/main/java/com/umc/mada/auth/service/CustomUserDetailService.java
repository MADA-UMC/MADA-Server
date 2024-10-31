package com.umc.mada.auth.service;

import com.umc.mada.auth.dto.OAuth2Attributes;
import com.umc.mada.custom.domain.CustomItem;
import com.umc.mada.custom.domain.HaveItem;
import com.umc.mada.custom.domain.WearingItem;
import com.umc.mada.custom.repository.CustomRepository;
import com.umc.mada.custom.repository.HaveItemRepository;
import com.umc.mada.custom.repository.WearingItemRepository;
import com.umc.mada.user.domain.CusomtUserDetails;
import com.umc.mada.user.domain.Role;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService extends DefaultOAuth2UserService{ // implements OAuth2UserService<OAuth2UserRequest, OAuth2User>
    private final UserRepository userRepository;
    private final WearingItemRepository wearingItemRepository;
    private final CustomRepository customRepository;
    private final HaveItemRepository haveItemRepository;

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
//        Optional<User> userOptional = userRepository.findByAuthIdAndAccountExpired(oAuth2Attributes.getAuthId(), false);

        User user;
        boolean newUser=false;
        //가입되어 있는 경우
        if(userOptional.isPresent()){
            user = userOptional.get();
            //유저 정보 업데이트
            if(provider.equals("google")){
                user = user.update(oAuth2Attributes.getAuthId(), oAuth2Attributes.getEmail(), userRequest.getAccessToken().getTokenValue());
            }else{
                user = user.update(oAuth2Attributes.getAuthId(), oAuth2Attributes.getEmail());
            }
            userRepository.save(user);
        } else{
            //첫 로그인인 경우 사용자를 회원가입(등록)한다.
            newUser = true;
            if(provider.equals("google")){
                user = createUser(oAuth2Attributes, provider, userRequest.getAccessToken().getTokenValue());
            }else{
                user = createUser(oAuth2Attributes, provider);
            }
            //사용자의 기본 데이터를 세팅한다.
            setUserData(user);
        }
//
//        return new DefaultOAuth2User(Collections.singleton(
//                new SimpleGrantedAuthority(user.getRole().getKey())),oAuth2Attributes.getAttributes(), oAuth2Attributes.getNameAttributeKey());
        return CusomtUserDetails.create(user, oAuth2Attributes.getAttributes(), newUser);
    }

    private User createUser(OAuth2Attributes oAuth2Attributes, String authProvider, String accessToken){
        User user = User.builder()
                    .authId(oAuth2Attributes.getAuthId())
                    .nickname(oAuth2Attributes.getName())
                    .email(oAuth2Attributes.getEmail())
                    .role(Role.USER)
                    .provider(authProvider)
                    .googleAccessToken(accessToken)
                    .build();
        return userRepository.save(user);
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

    private void setUserData(User user){
        List<CustomItem> customItems = customRepository.findAll();
        customItems.forEach(item->wearingItemRepository.save(WearingItem.builder().user(user).customItem(item).build()));
    }

    private void setUserDataDefault(User user){
        //사용자 캐릭터 디폴트 설정하기
        List<CustomItem> defaultItems = customRepository.findByUnlockCondition(CustomItem.ItemUnlockCondition.DEFAULT);

        for(CustomItem defaultItem: defaultItems){
            wearingItemRepository.save(WearingItem.builder().user(user).customItem(defaultItem).build());
        }

        //사용자 기본 제공 커스텀 아이템 세팅하기
        List<CustomItem> basicItems = customRepository.findByUnlockCondition(CustomItem.ItemUnlockCondition.BASIC);
        for(CustomItem basicItem : basicItems){
            haveItemRepository.save(HaveItem.builder().user(user).customItem(basicItem).build());
        }
    }
}
