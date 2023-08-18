package com.umc.mada.user.dto.nickname;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NicknameRequestDto {
    @NotBlank(message = "닉네임이 존재해야 합니다.")
    @Pattern(regexp = "^[0-9a-zA-Z가-힣]{1,10}", message = "닉네임은 10자 이하 한글, 숫자, 영어로만 이루어져야 합니다.")
    private String nickname;
}
