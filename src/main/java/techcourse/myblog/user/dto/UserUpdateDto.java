package techcourse.myblog.user.dto;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.user.domain.User;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserUpdateDto {
    @Pattern(regexp = "[A-Za-zㄱ-ㅎㅏ-ㅣ가-힣]{2,10}",
            message = "올바른 이름 형식이 아닙니다.")
    private String name;

    public User toUser(long id, String email, String password) {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .build();
    }
}
