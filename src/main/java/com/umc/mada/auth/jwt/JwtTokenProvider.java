package com.umc.mada.auth.jwt;

import com.umc.mada.user.domain.Role;
//import com.umc.mada.user.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private static final String AUTHORITIES_KEY = "authority"; //role로 줄 예정
    private Long ACCESS_TOKEN_EXPIRE_TIME = 1000L*60*600; //10hour
    private Long REFRESH_TOKEN_EXPIRE_TIME = 1000L*60*60*24*14; //14day

    private Key key;


    @PostConstruct
    protected void init(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //accesstoken 생성
    public String createAccessToken(OAuth2User oAuth2User){
        return createToken(oAuth2User, ACCESS_TOKEN_EXPIRE_TIME);
    }

    //refreshToken 생성
    public String createRefreshToken(OAuth2User oAuth2User){
        return createToken(oAuth2User, REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String createToken(OAuth2User oAuth2User, long expireTime){
        Map<String, Object> attributes = oAuth2User.getAttributes();

        //토큰을 발급하는 시간을 기준으로 토큰 유효기간 설정
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireTime);

        //claim에 넣을 정보 설정하기
//        Claims claims = Jwts.claims().setSubject(String.valueOf(user.getAuthid()));
//        claims.put("role",user.getRole());

//        Claims claims = Jwts.claims().setSubject((String)attributes.get("id"));
        Claims claims = Jwts.claims().setSubject("sfsw");
        claims.put(AUTHORITIES_KEY, Role.USER);

        return Jwts.builder()
//                .setSubject(auth) //
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }

    //request 헤더에서 토큰 parsing
    public String resolveToken(HttpServletRequest request){
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

//        if(!StringUtils.hasText(token) || !token.startsWith("Bearer ")){
//            throw new IllegalStateException("토큰이 없습니다.");
//        }

        return token.substring(7);
    }

    //토큰 검증
    public boolean validateToken(final String token){
        try{
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        }catch (JwtException | IllegalArgumentException e){//추후에 log에 기록남도록 하기
            return false;
        }
    }

    // 해당 함수는 parseClaims()를 쓰도록 바꿔도 될듯..? 거의 비슷해서
    //사용자 정보(id)를 반환
    public String getPayload(final String token){
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        }catch (ExpiredJwtException e){
            return e.getClaims().getSubject();
        }catch (JwtException e){
            throw new RuntimeException("유효하지 않은 토큰입니다.");//log에 기록남도록 하기
        }
    }

    //access token에 들어있는 정보를 꺼내서 authentication 만들기
    public Authentication getAuthentication(String token){
        //UserDetails userDetails = userDetailsService.loadUserBy
        Claims claims = parseClaims(token);
        String authId = claims.getSubject();

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        User user = new User(authId, "", authorities);

        return new UsernamePasswordAuthenticationToken(user, "", authorities);
    }

    //jwt 토큰 복호화한 후 정보 추출
    private Claims parseClaims(String accessToken){
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        } catch (JwtException e){
            throw new RuntimeException("유효하지 않은 토큰입니다.");//log에 기록남도록 하기
        }
    }
}
