package techcourse.myblog.users;

import lombok.*;

import javax.persistence.*;
import java.util.regex.Pattern;

@Entity
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}";
    static final String NAME_REGEX = "[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z]{2,10}";
    static final String EMAIL_NOT_MATCH_MESSAGE = "메일의 양식을 지켜주세요.";
    static final String NAME_NOT_MATCH_MESSAGE = "이름은 2자이상 10자이하이며, 숫자나 특수문자가 포함될 수 없습니다.";
    static final String PASSWORD_NOT_MATCH_MESSAGE = "비밀번호는 8자 이상의 소문자,대문자,숫자,특수문자의 조합이여야 합니다.";

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    static String authenticate(final String password) {
        return password;
    }

    @Builder
    private User(final Long id, final String email, final String name, final String password) {
        if (id == null) {
            validateName(name);
            validatePassword(password);
        }

        this.email = email;
        this.name = name;
        this.password = password;
    }

    private void validatePassword(final String password) {
        if (!PASSWORD_PATTERN.matcher(password).find()) {
            throw new IllegalArgumentException(PASSWORD_NOT_MATCH_MESSAGE);
        }
    }

    private void validateName(final String name) {
        if (!NAME_PATTERN.matcher(name).find()) {
            throw new IllegalArgumentException(NAME_NOT_MATCH_MESSAGE);
        }
    }

    void update(final User other) {
        this.name = other.name;
    }
}
