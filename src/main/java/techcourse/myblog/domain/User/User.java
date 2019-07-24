package techcourse.myblog.domain.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import techcourse.myblog.exception.UserCreationException;

import javax.persistence.*;
import java.util.Objects;
import java.util.regex.Pattern;

@Entity
public class User {
    private static final Logger log = LoggerFactory.getLogger(User.class);

    private static final int NAME_MIN_LENGTH = 2;
    private static final int NAME_MAX_LENGTH = 8;
    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final Pattern NAME_PATTERN = Pattern.compile("^[가-힣|a-zA-Z]+$");
    private static final Pattern PASSWORD_PATTERN
            = Pattern.compile(
            "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s)(?=.*[!@#\\$%\\^&\\*])(?!.*[가-힣])(?!.*[ㄱ-ㅎ])(?!.*[ㅏ-ㅣ]).*$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    public User() {
    }

    public User(final String name, final String email, final String password) {
        validate(name, password);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User update(final String name) {
        validateName(name);
        this.name = name;
        return this;
    }

    public User update(final User user) {
        validate(user.name, user.password);
        this.name = user.name;
        this.email = user.email;
        this.password = user.password;
        return this;
    }

    private void validate(final String name, final String password) {
        validateName(name);
        validatePassword(password);
    }

    private void validateName(final String name) {
        log.debug("name in User.class : {}", name);
        if (name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) {
            throw new UserCreationException("이름의 길이는 " + NAME_MIN_LENGTH +
                    "이상 " + NAME_MAX_LENGTH + "이하로 작성하세요");
        }

        if (!NAME_PATTERN.matcher(name).find()) {
            throw new UserCreationException("이름은 한글 또는 영어이름으로 입력하세요 (실명)");
        }
    }

    private void validatePassword(final String password) {
        log.debug("password in User.class : {}", password);
        if (password.length() < PASSWORD_MIN_LENGTH) {
            throw new UserCreationException("비밀번호의 길이는 " + PASSWORD_MIN_LENGTH +
                    "이상으로 작성해주세요");
        }

        if (!PASSWORD_PATTERN.matcher(password).find()) {
            throw new UserCreationException(
                    "비밀번호는 영어 소문자, 대문자, 숫자, 특수문자를 하나 이상 포함합니다");
        }
    }

    public boolean isSamePassword(final String password) {
        return this.password.equals(password);
    }

    public Long getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return name.equals(user.name) &&
                email.equals(user.email) &&
                password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
