package techcourse.myblog.domain;

import techcourse.myblog.service.exception.UserArgumentException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static techcourse.myblog.service.exception.UserArgumentException.*;

@Entity
public class User {
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 10;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final String KOREAN_ENGLISH_REGEX = "^[(ㄱ-ㅎ가-힣a-zA-Z)]+$";
    private static final String LOWER_CASE_REGEX = "[(a-z)]+";
    private static final String UPPER_CASE_REGEX = "[(A-Z)]+";
    private static final String NUMBER_REGEX = "[(0-9)]+";
    private static final String SPECIAL_CHARACTER_REGEX = "[ `~!@#[$]%\\^&[*]\\(\\)_-[+]=\\{\\}\\[\\][|]'\":;,.<>/?]+";
    private static final String KOREAN_REGEX = "[(ㄱ-ㅎ가-힣)]+";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;

    public User() {
    }

    public User(String name, String email, String password) {
        checkValidName(name);
        checkValidPassword(password);

        this.name = name;
        this.email = email;
        this.password = password;
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
        if (!matchRegex(name, KOREAN_ENGLISH_REGEX)) {
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
        if (!matchRegex(password, LOWER_CASE_REGEX)
                || !matchRegex(password, UPPER_CASE_REGEX)
                || !matchRegex(password, NUMBER_REGEX)
                || !matchRegex(password, SPECIAL_CHARACTER_REGEX)
                || matchRegex(password, KOREAN_REGEX)) {
            throw new UserArgumentException(INVALID_PASSWORD_MESSAGE);
        }
    }

    private boolean matchRegex(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
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

    public void setName(String name) {
        checkValidName(name);
        this.name = name;
    }
}
