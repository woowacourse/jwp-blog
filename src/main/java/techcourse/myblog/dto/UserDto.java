package techcourse.myblog.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDto {
    private static final int MIN_NAME_SIZE = 2;
    private static final int MAX_NAME_SIZE = 10;
    private static final String NAME_PATTERN = "^[가-힣a-zA-Z]+$";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,}$";
    private static final String ERROR_MESSAGE_ABOUT_NAME_SIZE = "이름은 2글자 이상 10글자 이하이어야 합니다.";
    private static final String ERROR_MESSAGE_ABOUT_NAME_PATTERN = "이름은 한글 또는 영어만 입력이 가능합니다.";
    private static final String ERROR_MESSAGE_ABOUT_EMAIL = "이메일 형식이 아닙니다.";
    private static final String ERROR_MESSAGE_ABOUT_PASSWORD_PATTERN = "비밀번호는 8글자 이상으로 특수문자, 숫자, 소문자, 대문자가 포함되어야 합니다.";

    @Size(min = MIN_NAME_SIZE, max = MAX_NAME_SIZE, message = ERROR_MESSAGE_ABOUT_NAME_SIZE)
    @Pattern(regexp = NAME_PATTERN, message = ERROR_MESSAGE_ABOUT_NAME_PATTERN)
    private String name;

    @Email(message = ERROR_MESSAGE_ABOUT_EMAIL)
    private String email;

    @Pattern(regexp = PASSWORD_PATTERN, message = ERROR_MESSAGE_ABOUT_PASSWORD_PATTERN)
    private String password;

    @Pattern(regexp = PASSWORD_PATTERN, message = ERROR_MESSAGE_ABOUT_PASSWORD_PATTERN)
    private String passwordConfirm;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
