package techcourse.myblog.domain;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class User {
    private static final int MAX_NAME_LENGTH = 10;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final Pattern NAME_PATTERN = Pattern.compile("[^ !@#$%^&*(),.?\\\":{}|<>0-9]{2,10}");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    @Column(unique = true)
    private String email;

    private User() {
    }

    public User(String name, String password, String email) {
        validateName(name);
        validatePassword(password);
        validateEmail(email);
        this.name = name;
        this.password = password;
        this.email = email;
    }

    private void validateName(String name) {
        isEmpty(name);
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("이름은 " + MAX_NAME_LENGTH + "자 미만이어야 합니다.");
        }

        Matcher matcher = NAME_PATTERN.matcher(name);
        if (!matcher.find()) {
            throw new IllegalArgumentException("이름은 알파벳만 가능합니다.");
        }
    }

    private void validatePassword(String password) {
        isEmpty(password);

        if (password.length() <= MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("비밀번호는 " + MIN_PASSWORD_LENGTH + "자 이상이어야 합니다.");
        }

        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        if (!matcher.find()) {
            throw new IllegalArgumentException("비밀번호는 영어 대/소문자, 숫자, 특수문자를 포함해야 합니다.");
        }
    }

    private void validateEmail(String email) {
        isEmpty(email);
    }

    private void isEmpty(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("아무것도 입력하지 않았습니다.");
        }
    }

    public void changeName(String name) {
        validateName(name);
        this.name = name;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
