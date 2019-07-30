package techcourse.myblog.domain;

import org.hibernate.validator.constraints.Length;
import techcourse.myblog.dto.UserDto;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
public class User {
    private static final String BLANK_NAME = "이름을 입력해주세요.";
    private static final String NOT_VALID_EMAIL = "올바른 이메일 주소를 입력해주세요.";
    private static final String NOT_VALID_NAME = "제대로 된 이름을 입력해주세요.";
    private static final String NAME_LENGTH = "이름은 2자 이상 10자 이하의 길이여야 합니다.";
    private static final String PASSWORD_RULE = "패스워드는 영어 대소문자와 한글과 숫자를 모두 섞어 8글자 이상이어야 합니다.";

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Length(min = 2, max = 10, message = NAME_LENGTH)
    @NotBlank(message = BLANK_NAME)
    @Pattern(regexp = "^[a-zA-Z가-힣]+$", message = NOT_VALID_NAME)
    @Valid
    private String username;

    // TODO: 차후 패스워드 암호화
    @Length(min = 8, message = PASSWORD_RULE) // 비밀번호는 8자 이상의 소문자, 대문자, 숫자, 특수문자의 조합이다.
    @Pattern(regexp = "^(?=.*[\\p{Ll}])(?=.*[\\p{Lu}])(?=.*[\\p{N}])(?=.*[\\p{S}\\p{P}])[\\p{Ll}\\p{Lu}\\p{N}\\p{S}\\p{P}]+$", message = PASSWORD_RULE)
    @Valid
    private String password;

    @NotBlank(message = NOT_VALID_EMAIL)
    @Email(message = NOT_VALID_EMAIL)
    @Valid
    private String email;

    @OneToMany(mappedBy = "author")
    private List<Article> articles = new ArrayList<>();

    public User() {}

    public User(final String username, final String email, final String password) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(final UserDto userDto) {
        update(userDto);
    }

    public User update(final UserDto userDto) {
        this.username = userDto.getUsername();
        this.password = userDto.getPassword();
        this.email = userDto.getEmail();
        return this;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<Article> getArticles() {
        return this.articles;
    }

    public void addArticle(final Article article) {
        articles.add(article);
    }

    @Override
    public boolean equals(final Object another) {
        if (this == another) return true;
        if (!(another instanceof User)) return false;
        final User user = (User) another;
        return id.equals(user.id) &&
                Objects.equals(username, user.username) &&
                password.equals(user.password) &&
                email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("username='" + username + "'")
                .add("password='" + password + "'")
                .add("email='" + email + "'")
                .toString();
    }
}
