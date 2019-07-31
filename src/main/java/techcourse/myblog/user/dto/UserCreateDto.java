package techcourse.myblog.user.dto;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.user.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserCreateDto {
    @Email(message = "올바른 email 형식이 아닙니다.")
    private String email;

    @Pattern(regexp = ".*(?=^.{8,}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*",
            message = "올바른 비밀번호 형식이 아닙니다.")
    private String password;

    @Pattern(regexp = "[A-Za-zㄱ-ㅎㅏ-ㅣ가-힣]{2,10}",
            message = "올바른 이름 형식이 아닙니다.")
    private String name;

    public User toUser() {
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }
}
