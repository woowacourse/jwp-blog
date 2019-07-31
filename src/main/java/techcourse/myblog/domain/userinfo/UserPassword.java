package techcourse.myblog.domain.userinfo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.exception.IllegalUserParamsException;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.regex.Pattern;

@EqualsAndHashCode(of = "password")
@Getter
@NoArgsConstructor
@Embeddable
public class UserPassword {

    @Transient
    private static final Pattern PASSWORD_PATTERN
            = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$");
    @Transient
    private static final String ERROR_ILLEGAL_USER_PASSWORD_MESSAGE = "비밀번호는 8자 이상의 문자, 숫자, 특수문자 조합으로 입력해 주세요!";

    private String password;

    public UserPassword(String password) {
        if (isNotCorrect(password)) {
            throw new IllegalUserParamsException(ERROR_ILLEGAL_USER_PASSWORD_MESSAGE);
        }
        this.password = password;
    }

    private boolean isNotCorrect(String name) {
        return (name != null) && !PASSWORD_PATTERN.matcher(name).find();
    }
}
