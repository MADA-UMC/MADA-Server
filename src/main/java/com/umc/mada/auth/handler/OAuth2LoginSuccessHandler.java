package com.umc.mada.auth.handler;

import com.umc.mada.auth.dto.OAuth2Attributes;
import com.umc.mada.auth.jwt.JwtTokenProvider;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.SysexMessage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
//        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(provider, userNameAttributeName, oAuth2User.getAttributes());
//        Optional<User> findUser = userRepository.findByAuthId()

        //jwt 생성
        String accessToken = jwtTokenProvider.createAccessToken(oAuth2User);
        String refreshToken = jwtTokenProvider.createRefreshToken(oAuth2User);

        //refreshtoken을 DB에 저장해야함

//        tokenResponse(response, accessToken);
        String redirectUrl = makeRedirectUrl(accessToken);

//        @Async
        response.setHeader("Content-type", "text/plain");
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken);
        response.sendRedirect(redirectUrl);
        PrintWriter writer = response.getWriter();
        writer.println("ok");
        System.out.println("성공");
        System.out.println(response.getHeader(HttpHeaders.AUTHORIZATION));
    }

    private String makeRedirectUrl(String accessToken){
        return UriComponentsBuilder.fromUriString("http://localhost:8080/login/test") //"http://localhost:8080/login/oauth2/code/google"
                .queryParam("token", accessToken)
                .build().toUriString();
    }

//    private String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
//        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
//                .map(Cookie::getValue);
//
//    }

//    private void tokenResponse(HttpServletResponse response, String accessToken){
//        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken);
//    }
}
