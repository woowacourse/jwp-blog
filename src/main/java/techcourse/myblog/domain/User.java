package techcourse.myblog.domain;

import techcourse.myblog.exception.InvalidUserDataException;

import javax.persistence.*;
import java.util.Objects;
import java.util.regex.Pattern;

@Entity
public class User extends Auditable {
    private static Pattern PATTERN_NAME = Pattern.compile("^[a-zA-Zㄱ-ㅎ가-힣]*$");
    private static Pattern PATTERN_EMAIL = Pattern.compile("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$");
    private static Pattern PATTERN_PASSWORD = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,}$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public User() {
    }

    public User(long userId, String name, String email, String password) {
        validateName(name);
        validateEmail(email);
        validatePassword(password);

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validateName(String name) {
        if (!PATTERN_NAME.matcher(name).matches()) {
            throw new InvalidUserDataException("이름은 2~10자로 제한하며 숫자나 특수문자가 포함될 수 없습니다.");
        }
    }

    private void validateEmail(String email) {
        if (!PATTERN_EMAIL.matcher(email).matches()) {
            throw new InvalidUserDataException("올바른 형식의 이메일을 입력해 주세요.");
        }
    }

    private void validatePassword(String password) {
        if (!PATTERN_PASSWORD.matcher(password).matches()) {
            throw new InvalidUserDataException("비밀번호는 8자 이상의 소문자, 대문자, 숫자, 특수문자의 조합이어야 합니다.");
        }
    }

    public User update(User updatedUser) {
        this.name = updatedUser.getName();
        this.password = updatedUser.getPassword();
        return this;
    }

    public long getUserId() {
        return userId;
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
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, email, password);
    }
}
