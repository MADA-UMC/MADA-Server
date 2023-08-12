//package com.umc.mada.config;
//
//import com.umc.mada.auth.handler.OAuth2LoginSuccessHandler;
//import com.umc.mada.auth.jwt.JwtAuthenticationFilter;
//import com.umc.mada.auth.jwt.JwtTokenProvider;
//import com.umc.mada.auth.service.CustomUserDetailService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.*;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.*;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@RequiredArgsConstructor
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
//public class SecurityConfig{
//    private final JwtTokenProvider jwtTokenProvider;
//    private final CustomUserDetailService customUserDetailService;
//    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
//
//    private static final String[] ignores = {
//            "/swagger-ui/**"
//    };
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().antMatchers(ignores);
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .cors()
//                .and()
//                .csrf().disable()
//                .httpBasic().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
//                .and()
//
//                .authorizeRequests()
//                //.antMatchers("/","/login").permitAll()
//                //.anyRequest().hasRole("USER")
//                .anyRequest().permitAll()
////                .anyRequest().authenticated()
//
//                .and()
//                .oauth2Login(oauth2 -> oauth2
//                        .userInfoEndpoint()
//                        .userService(customUserDetailService)
//                        .and()
//                        .successHandler(oAuth2LoginSuccessHandler)
//                )
//
//                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//}

package com.umc.mada.config;

import com.umc.mada.auth.handler.OAuth2LoginSuccessHandler;
import com.umc.mada.auth.jwt.JwtAuthenticationFilter;
import com.umc.mada.auth.jwt.JwtTokenProvider;
import com.umc.mada.auth.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig{
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    private static final String[] ignores = {
            "/favicon.ico",
            "/error", "/swagger-ui/**","/swagger-resources/**",
            "/user/test"
    };

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(ignores);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //.antMatchers("/","/login").permitAll()
                //.anyRequest().hasRole("USER")
                .anyRequest().permitAll()
//                .anyRequest().authenticated()

                .and()
                .logout()
                .logoutSuccessUrl("http://localhost:8080/oauth2/authorization/naver")

                .and()
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint()
                        .userService(customUserDetailService)
                        .and()
                        .successHandler(oAuth2LoginSuccessHandler)
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
