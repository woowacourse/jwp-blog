package techcourse.myblog.domain.user;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.exception.UserArgumentException;

import javax.persistence.*;
import java.util.List;
import java.util.regex.Pattern;

import static techcourse.myblog.domain.exception.UserArgumentException.*;

@Entity
public class User {
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 10;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final Pattern KOREAN_ENGLISH_PATTERN = Pattern.compile("^[(ㄱ-ㅎ가-힣a-zA-Z)]+$");
    private static final Pattern LOWER_CASE_PATTERN = Pattern.compile("[(a-z)]+");
    private static final Pattern UPPER_CASE_PATTERN = Pattern.compile("[(A-Z)]+");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[(0-9)]+");
    private static final Pattern SPECIAL_CHARACTER_PATTERN =
            Pattern.compile("[ `~!@#[$]%\\^&[*]\\(\\)_-[+]=\\{\\}\\[\\][|]'\":;,.<>/?]+");
    private static final Pattern KOREAN_PATTERN = Pattern.compile("[(ㄱ-ㅎ가-힣)]+");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Article> articles;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    private User() {
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
        if (!matchRegex(name, KOREAN_ENGLISH_PATTERN)) {
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
        if (!matchRegex(password, LOWER_CASE_PATTERN)
                || !matchRegex(password, UPPER_CASE_PATTERN)
                || !matchRegex(password, NUMBER_PATTERN)
                || !matchRegex(password, SPECIAL_CHARACTER_PATTERN)
                || matchRegex(password, KOREAN_PATTERN)) {
            throw new UserArgumentException(INVALID_PASSWORD_MESSAGE);
        }
    }

    private boolean matchRegex(String input, Pattern pattern) {
        return pattern.matcher(input).find();
    }

    public boolean matchId(Long userId) {
        return this.id.equals(userId);
    }

    public void updateName(String name) {
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
}
