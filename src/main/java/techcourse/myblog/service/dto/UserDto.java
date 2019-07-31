package techcourse.myblog.service.dto;

import techcourse.myblog.support.validation.UserInfo;
import techcourse.myblog.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserDto {
    static final String NO_INPUT_MESSAGE = "입력이 필요합니다.";
    public static final String NAME_CONSTRAINT_MESSAGE = "이름은 2~10자, 숫자나 특수문자가 포함될 수 없습니다.";
    public static final String EMAIL_CONSTRAINT_MESSAGE = "이메일 양식을 지켜주세요.";
    public static final String PASSWORD_CONSTRAINT_MESSAGE = "비밀번호는 8자 이상, 소문자, 대문자, 숫자, 특수문자의 조합으로 입력하세요.";

    @NotBlank(message = NO_INPUT_MESSAGE,
            groups={UserInfo.class})
    @Pattern(regexp = "^[^ \\-!@#$%^&*(),.?\\\":{}|<>0-9]{2,10}$",
            message = NAME_CONSTRAINT_MESSAGE,
            groups={UserInfo.class})
    private String name;

    @NotBlank(message = NO_INPUT_MESSAGE)
    @Email(message = EMAIL_CONSTRAINT_MESSAGE)
    private String email;

    @NotBlank(message = NO_INPUT_MESSAGE)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}$",
            message = PASSWORD_CONSTRAINT_MESSAGE)
    private String password;

    public UserDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public User toUser() {
        return new User(name, email, password);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
