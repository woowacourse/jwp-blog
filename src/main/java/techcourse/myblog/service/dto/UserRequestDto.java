package techcourse.myblog.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {
    public static final String NAME_ERROR_MSG = "2글자 이상, 10글자 이하로 입력하세요. 숫자와 특수문자는 입력할 수 없습니다.";
    public static final String PASSWORD_ERROR_MSG = "소문자, 대문자, 숫자, 특수문자가 조합된 8글자 이상을 입력하세요.";
    public static final String EMAIL_ERROR_MSG = "이메일의 형식에 맞춰 입력하세요.";
    private static final long BLANK_USER_ID = 0;

    private long id = BLANK_USER_ID;
    @Pattern(regexp = "^[a-zA-Z가-힣]{2,10}$", message = NAME_ERROR_MSG)
    private String name;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~`!@#$%\\^&*()-]).{8,}$", message = PASSWORD_ERROR_MSG)
    private String password;
    @Email(message = EMAIL_ERROR_MSG)
    private String email;

    public UserRequestDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.password = user.getPassword();
        this.email = user.getEmail();
    }

    public User toUser() {
        return new User(name, password, email);
    }
}
