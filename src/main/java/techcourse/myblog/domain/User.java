package techcourse.myblog.domain;

import javax.persistence.*;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * NOTE:
 *
 * 유저네임에 대한 제약사항은 다음과 같이 가정
 * 길이는 2~10글자이며 영문 대소문자만 허용
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String email;
    private String password;

    @Transient
    private static Pattern namePattern;

    @Transient
    private static Pattern passwordPattern;

    static {
        namePattern = Pattern.compile("^[a-zA-Z ]*$");
        // ref. http://html5pattern.com/Passwords
        //TODO 정규표현식 숫자가 포함되지 않아도 통과 (TestPasswod!)
        passwordPattern = Pattern.compile("(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");
    }


    public static User of(String userName, String email, String password) {
        User user = new User();
        user.userName = userName;
        user.email = email;
        user.password = password;
        return user;
    }

    public static User of(String userName, String email, String password, Function<String, Boolean> emailDuplicateChecker) {
        if (emailDuplicateChecker.apply(email)) {
            throw new UserCreationConstraintException("이미 등록된 이메일입니다.");
        }

        if (isInvalidUserName(userName)) {
            throw new UserCreationConstraintException("올바르지 않은 유저네임입니다.");
        }

        if (isInvalidPassword(password)) {
            throw new UserCreationConstraintException("올바르지 않은 비밀번호입니다.");
        }

        return of(userName, email, password);
    }

    private static boolean isInvalidUserName(String userName) {
        if (userName.length() < 2 || userName.length() > 10) {
            return true;
        }
        if (!namePattern.matcher(userName).matches()) {
            return true;
        }
        return false;
    }

    private static boolean isInvalidPassword(String password) {
        if (password.length() < 8) {
            return true;
        }

        return !passwordPattern.matcher(password).matches();
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
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
        return Objects.equals(id, user.id) &&
            Objects.equals(userName, user.userName) &&
            Objects.equals(email, user.email) &&
            Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, email, password);
    }

    public boolean authentication(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    public static class UserCreationConstraintException extends IllegalArgumentException {
        public UserCreationConstraintException(String message) {
            super(message);
        }
    }
}
