package techcourse.myblog.domain.user;

import org.hibernate.validator.constraints.Length;
import techcourse.myblog.domain.BaseTimeEntity;
import techcourse.myblog.domain.exception.UserArgumentException;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Objects;
import java.util.regex.Pattern;

import static techcourse.myblog.domain.exception.UserArgumentException.*;

@Entity
public class User extends BaseTimeEntity {
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 10;
    private static final int MIN_PASSWORD_LENGTH = 8;

    private static final String NAME_REGEX = "^[가-힣|a-zA-Z]+$";
    private static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s)" +
            "(?=.*[`~!@#[$]%\\^&[*]\\(\\)_-[+]=\\{\\}\\[\\][|]'\":;,.<>/?])(?!.*[가-힣])(?!.*[ㄱ-ㅎ])(?!.*[ㅏ-ㅣ]).*$";

    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    @Length(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    @javax.validation.constraints.Pattern(regexp = NAME_REGEX)
    private String name;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @Length(min = MIN_PASSWORD_LENGTH)
    @javax.validation.constraints.Pattern(regexp = PASSWORD_REGEX)
    private String password;

    protected User() {
    }

    public User(String name, String email, String password) {
        checkValidName(name);
        checkValidPassword(password);

        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean matchPassword(final String password) {
        return this.password.equals(password);
    }

    private void checkValidName(String name) {
        checkValidNameLength(name);
        checkValidNameCharacters(name);
    }

    private void checkValidNameLength(String name) {
        int nameLength = name.length();
        if (nameLength < MIN_NAME_LENGTH || nameLength > MAX_NAME_LENGTH) {
            throw new UserArgumentException(INVALID_NAME_LENGTH_MESSAGE);
        }
    }

    private void checkValidNameCharacters(String name) {
        if (!matchRegex(name, NAME_PATTERN)) {
            throw new UserArgumentException(NAME_INCLUDE_INVALID_CHARACTERS_MESSAGE);
        }
    }

    private void checkValidPassword(String password) {
        checkValidPasswordLength(password);
        checkValidPasswordCharacters(password);
    }

    private void checkValidPasswordLength(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new UserArgumentException(INVALID_PASSWORD_LENGTH_MESSAGE);
        }
    }

    private void checkValidPasswordCharacters(String password) {
        if (!matchRegex(password, PASSWORD_PATTERN)) {
            throw new UserArgumentException(INVALID_PASSWORD_MESSAGE);
        }
    }

    private boolean matchRegex(String input, Pattern pattern) {
        return pattern.matcher(input).find();
    }

    public void update(User other) {
        updateName(other.name);
    }

    private void updateName(String name) {
        checkValidName(name);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
