package techcourse.myblog.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.regex.Pattern;

@Entity
public class User {
    public static final String EMAIL_PATTERN = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
    private static final String PASSWORD_PATTERN = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
    private static final String NAME_PATTERN = "^[a-zA-Z]{2,10}$";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userName;
    private String email;
    private String password;

    public User() {
    }

    public User(UserDto userDto) {
        this(userDto.getUserName(), userDto.getEmail(), userDto.getPassword());
        checkConfirmPassword(userDto);
    }

    public User(String userName, String email, String password) {
        checkValidEmail(email);
        checkValidUserName(userName);
        checkValidPassword(password);

        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    private void checkConfirmPassword(UserDto userDto) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    private void checkValidEmail(String email) {
        if (!Pattern.matches(EMAIL_PATTERN, email)) {
            throw new IllegalArgumentException("형식에 맞는 이메일이 아닙니다.");
        }
    }

    private void checkValidUserName(String userName) {
        if (!Pattern.matches(NAME_PATTERN, userName)) {
            throw new IllegalArgumentException("형식에 맞는 이름이 아닙니다.");

        }
    }

    private void checkValidPassword(String password) {
        if (!Pattern.matches(PASSWORD_PATTERN, password)) {
            throw new IllegalArgumentException("형식에 맞는 비밀번호가 아닙니다.");
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
