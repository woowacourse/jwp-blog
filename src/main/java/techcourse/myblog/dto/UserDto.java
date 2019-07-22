package techcourse.myblog.dto;

import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    public static final String EMAIL_PATTERN = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
    public static final String NAME_PATTERN = "^.{2,10}$";
    public static final String PASSWORD_PATTERN = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";

    @Pattern(regexp = EMAIL_PATTERN, message = "email형식에 맞는 이름이 아닙니다.")
    private String email;
    @Size(min = 2, max = 10, message = "닉네임은 2글자 이상 10글자 이하 입니다.")
    @Pattern(regexp = NAME_PATTERN, message = "형식에 맞는 이름이 아닙니다.")
    private String name;
    @Pattern(regexp = PASSWORD_PATTERN, message = "올바른 password에 맞는 형식이 아닙니다.")
    private String password;
}
