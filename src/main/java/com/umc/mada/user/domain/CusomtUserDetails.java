package com.umc.mada.user.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public class CusomtUserDetails implements UserDetails, OAuth2User {
    private User user;
    private String authId;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private boolean newUser;

    public CusomtUserDetails(User user, String authId, Collection<? extends GrantedAuthority> authorities, boolean newUser){
        this.user = user;
        this.authId = authId;
        this.authorities = authorities;
        this.newUser = newUser;
    }

    public static CusomtUserDetails create(User user, boolean newUser){
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(Role.USER.getKey()));
        return new CusomtUserDetails(user, user.getAuthId(), authorities, newUser);
    }

    public static CusomtUserDetails create(User user, Map<String, Object> attributes, boolean newUser){
        CusomtUserDetails userDetails = CusomtUserDetails.create(user, newUser);
        userDetails.attributes = attributes;
        return userDetails;
    }

    public boolean getNewUser(){
        return newUser;
    }

    @Override
    public String getName() {
        return authId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return String.valueOf(user.getId());
    }

    @Override
    public boolean isAccountNonExpired() { //계정이 만료되지 않았는지, true는 만료되지 않음
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //계정이 잠겨있지 않은지 리턴
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() { //계정이 사용가능한 계정인지, true면 사용가능한 계정
        return user.isAccount_expired();
    }
}
