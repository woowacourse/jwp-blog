package techcourse.myblog.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class UserDto {
    private static final String NAME_PATTERN = "^([a-zA-Z]){2,10}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,20}$";

    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(regexp = NAME_PATTERN, message = "영문자를 2-10로 입력해주세요.")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "패스워드를 입력해 주세요.")
    @Pattern(regexp = PASSWORD_PATTERN, message = "영어,특수문자,숫자를 8자 이상으로 써주세요.")
    private String password;
}
