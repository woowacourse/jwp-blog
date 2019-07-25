package techcourse.myblog.domain;

import lombok.*;
import techcourse.myblog.dto.UserEditParams;
import techcourse.myblog.exception.IllegalUserParamsException;

import javax.persistence.*;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
public class User {
    private static final Pattern NAME_PATTERN = Pattern.compile("[가-힣ㅣa-zA-Z]{2,10}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");
    private static final Pattern PASSWORD_PATTERN
            = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$");
    private static final String ERROR_ILLEGAL_USER_NAME_MESSAGE = "이름은 2~10자의 정확한 문자로 입력해 주세요!";
    private static final String ERROR_ILLEGAL_USER_EMAIL_MESSAGE = "이메일 형식에 맞게 입력해 주세요!";
    private static final String ERROR_ILLEGAL_USER_PASSWORD_MESSAGE = "비밀번호는 8자 이상의 문자, 숫자, 특수문자 조합으로 입력해 주세요!";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Builder
    public User(String name, String email, String password) {
        validateParams(name, email, password);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validateParams(String name, String email, String password) {
        if (isNotMatchedBetween(NAME_PATTERN, name)) {
            throw new IllegalUserParamsException(ERROR_ILLEGAL_USER_NAME_MESSAGE);
        }
        if (isNotMatchedBetween(EMAIL_PATTERN, email)) {
            throw new IllegalUserParamsException(ERROR_ILLEGAL_USER_EMAIL_MESSAGE);
        }
        if (isNotMatchedBetween(PASSWORD_PATTERN, password)) {
            throw new IllegalUserParamsException(ERROR_ILLEGAL_USER_PASSWORD_MESSAGE);
        }
    }

    private boolean isNotMatchedBetween(Pattern pattern, String userParam) {
        return !pattern.matcher(userParam).find();
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public void update(UserEditParams userEditParams) {
        this.name = userEditParams.getName();
    }
}
