package techcourse.myblog.domain.userinfo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.exception.IllegalUserParamsException;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.regex.Pattern;

@EqualsAndHashCode(of = "email")
@Getter
@NoArgsConstructor
@Embeddable
public class UserEmail {

    @Transient
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");
    @Transient
    private static final String ERROR_ILLEGAL_USER_EMAIL_MESSAGE = "이메일 형식에 맞게 입력해 주세요!";

    private String email;

    public UserEmail(String email) {
        if (isNotCorrect(email)) {
            throw new IllegalUserParamsException(ERROR_ILLEGAL_USER_EMAIL_MESSAGE);
        }
        this.email = email;
    }

    private boolean isNotCorrect(String name) {
        return (name != null) && !EMAIL_PATTERN.matcher(name).find();
    }

    public boolean match(String email) {
        return this.email.equals(email);
    }
}
