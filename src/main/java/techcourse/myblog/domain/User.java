package techcourse.myblog.domain;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * NOTE:
 * <p>
 * 유저네임에 대한 제약사항은 다음과 같이 가정
 * 길이는 2~10글자이며 영문 대소문자만 허용
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;

    public static User of(String username, String email, String password, Function<String, Boolean> emailDuplicateChecker) {
        if (emailDuplicateChecker.apply(email)) {
            throw new UserCreationConstraintException("이미 등록된 이메일입니다.");
        }

        User newUser = new User();
        newUser.email = email;
        newUser.username = username;
        newUser.password = password;

        return newUser;
    }

    public void update(User user) {
        this.username = user.username;
        this.email = user.email;
        this.password = user.password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean authenticate(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
            Objects.equals(username, user.username) &&
            Objects.equals(email, user.email) &&
            Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password);
    }

    public static class UserCreationConstraintException extends IllegalArgumentException {
        public UserCreationConstraintException(String message) {
            super(message);
        }
    }
}
