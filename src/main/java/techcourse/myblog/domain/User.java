package techcourse.myblog.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.exception.SignUpInputException;

import javax.persistence.*;
import java.util.Objects;
import java.util.regex.Pattern;

@Entity
@Getter
@NoArgsConstructor
public class User {
    public static final String NAME_PATTERN = "[a-zA-Z가-힣]{2,10}";
    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}";
    public static final String EMAIL_PATTERN = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Builder
    private User(final Long id, final String name, final String email, final String password) {
        vaildation(name, email, password);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void vaildation(String name, String email, String password) {
        if (isNotValidName(name) || isNotValidEmail(email) || isNotValidPassword(password)) {
            throw new SignUpInputException("올바르지 않은 입력 값 입니다.");
        }
    }

    private boolean isNotValidName(String name) {
        return !Pattern.matches(NAME_PATTERN, name);
    }

    private boolean isNotValidEmail(String email) {
        return !Pattern.matches(EMAIL_PATTERN, email);
    }

    private boolean isNotValidPassword(String password) {
        return !Pattern.matches(PASSWORD_PATTERN, password);
    }

    public boolean authenticate(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    public boolean equals(String email) {
        return this.email.equals(email);
    }
}