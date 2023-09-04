package com.umc.mada.auth.handler;

import com.umc.mada.auth.handler.jwt.JwtTokenProvider;
import com.umc.mada.user.domain.CusomtUserDetails;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
//        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(provider, userNameAttributeName, oAuth2User.getAttributes());
//        Optional<User> findUser = userRepository.findByAuthId()

        //jwt 생성
        String accessToken = jwtTokenProvider.createAccessToken(oAuth2User);
        String refreshToken = jwtTokenProvider.createRefreshToken(oAuth2User);

        //refreshtoken을 DB에 저장해야함
        Optional<User> userOptional = userRepository.findByAuthId(oAuth2User.getName());
        User user = userOptional.get(); //TODO: null값 체크하기
        user.setRefreshToken(refreshToken);

//        tokenResponse(response, accessToken);
        CusomtUserDetails cusomtUserDetails = (CusomtUserDetails) authentication.getPrincipal();
        String redirectUrl = makeRedirectUrl(accessToken, cusomtUserDetails.getNewUser());

        response.setHeader("Content-type", "text/plain");
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken);
        response.sendRedirect(redirectUrl);
        PrintWriter writer = response.getWriter();
        writer.println("ok");
//        @Async
//        response.setContentType("text/html;charset=UTF-8");
//        response.setHeader("Content-type", "text/plain");
//        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken);
////        response.setContentType("application/json;charset=UTF-8");
//
//        response.sendRedirect(redirectUrl);
//        PrintWriter writer = response.getWriter();
//        writer.println("ok");
//        PrintWriter out = response.getWriter();
//        //스트림에 텍스트를 기록
//        out.println(accessToken);
//        out.flush();
        System.out.println("성공");
        System.out.println(response.getHeader(HttpHeaders.AUTHORIZATION));
    }

    private String makeRedirectUrl(String accessToken, boolean newUser){
        if(newUser){ //회원가입한 유저라면 닉네임 입력받는 곳으로 리다이렉트
            return UriComponentsBuilder.fromUriString("http://www.madaumc.store/user/signup") //"http://localhost:8080/login/oauth2/code/google"
                    .queryParam("token", accessToken)
                    .build().toUriString();
        }
        return UriComponentsBuilder.fromUriString("http://www.madaumc.store/user/test") //"http://localhost:8080/login/oauth2/code/google"
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
