package com.umc.mada.auth.dto;

import com.umc.mada.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2Attributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String authId;
    private String name;
    private String email;

    @Builder
    public OAuth2Attributes(Map<String, Object> attributes, String nameAttributeKey, String authId, String name, String email){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.authId = authId;
        this.name = name;
        this.email = email;
    }

    //로그인 서비스가 카카오인지, 구글인지 네이버인지 구분해서 알맞게 매핑해준다.
    //provider는 google, kakao, naver
    //userNameAttributeName은 map의 키값이 되는 sub, response, id이다.
    public static OAuth2Attributes of(String provider, String userNameAttributeName, Map<String, Object> attributes){
        switch (provider){
            case "google":
                return ofGoogle(userNameAttributeName, attributes);
            case "kakao":
                return ofKakao(userNameAttributeName, attributes);
            case "naver":
                return ofNaver(userNameAttributeName, attributes);
            default:
                throw new RuntimeException();
        }
    }

    private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes){
        return OAuth2Attributes.builder()
                .authId((String) attributes.get("sub"))
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuth2Attributes ofKakao(String userNameAttributeName, Map<String, Object> attributes){
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attributes.builder()
                .authId((String) kakaoAccount.get("id"))
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuth2Attributes ofNaver(String userNameAttributeName, Map<String, Object> attributes){
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attributes.builder()
                .authId((String) response.get("id"))
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity(){
        return User.builder()
                .authId(authId)
                .nickname(name)
                .email(email)
                .build();
    }
}
