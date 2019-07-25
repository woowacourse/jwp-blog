package techcourse.myblog.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserDto {
    private static final String NAME_PATTERN = "^[a-zA-Zㄱ-ㅎ가-힣]{2,}$";
    private static final String EMAIL_PATTERN = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,}$";
    private static final String CANNOT_BE_NULL_MESSAGE = "입력값이 Null일 수는 없습니다.";
    private static final String INVALID_NAME_MESSAGE = "이름은 2~10자로 제한하며 숫자나 특수문자가 포함될 수 없습니다.";
    private static final String INVALID_EMAIL_MESSAGE = "올바른 형식의 이메일을 입력해 주세요.";
    private static final String INVALID_PASSWORD_MESSAGE = "비밀번호는 8자 이상의 소문자, 대문자, 숫자, 특수문자의 조합이어야 합니다.";

    private long userId;

    @NotNull(message = CANNOT_BE_NULL_MESSAGE)
    @Pattern(message = INVALID_NAME_MESSAGE, regexp = NAME_PATTERN)
    private String name;

    @NotNull(message = CANNOT_BE_NULL_MESSAGE)
    @Column(unique = true)
    @Pattern(message = INVALID_EMAIL_MESSAGE, regexp = EMAIL_PATTERN)
    private String email;

    @NotNull(message = CANNOT_BE_NULL_MESSAGE)
    @Pattern(message = INVALID_PASSWORD_MESSAGE, regexp = PASSWORD_PATTERN)
    private String password;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

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
}
