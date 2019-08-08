package techcourse.myblog.user.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;

@Embeddable
public class Password {
    private static final String PASSWORD_REGEX = ".*(?=^.{8,}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*";

    @Pattern(regexp = PASSWORD_REGEX)
    @Column(name = "password", nullable = false)
    private String password;

    public Password() {}

    private Password(final String password) {
        this.password = validatePassword(password);
    }

    public static Password of(final String password) {
        return new Password(password);
    }

    private String validatePassword(final String password) {
        if (!password.matches(PASSWORD_REGEX)) {
            throw new IllegalArgumentException("잘못된 형식의 비밀번호입니다.");
        }
        return password;
    }

    public boolean isCorrect(String password) {
        return this.password.equals(password);
    }

    public String getPassword() {
        return password;
    }
}