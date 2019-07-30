package techcourse.myblog.domain;

import org.hibernate.validator.constraints.Length;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.exception.SignUpException;

import javax.persistence.*;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class User {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[ㄱ-ㅎ가-힣a-zA-Z]{2,10}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}");
    private static final String INVALID_PASSWORD = "패스워드 : 8자 이상의 대문자, 소문자, 특수문자, 숫자 각각 1개 이상 포함";
    private static final String INVALID_NAME = "이름 : 2 ~ 10자의 한글 또는 영어(숫자, 특수문자 사용 불가)";

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false)
    @Length(min = 2, max = 10)
    private String name;
    @Column(nullable = false)
    @Length(min = 8)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    public User() {
    }

    public User(String name, String password, String email) {
        this.name = validateName(name);
        this.password = validatePassword(password);
        this.email = email;
    }

    private String validatePassword(String password) {
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        if (!matcher.find()) {
            throw new SignUpException(INVALID_PASSWORD);
        }
        return password;
    }

    private String validateName(String name) {
        Matcher matcher = NAME_PATTERN.matcher(name);
        if (!matcher.find()) {
            throw new SignUpException(INVALID_NAME);
        }
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void updateNameAndEmail(UserRequestDto userRequestDto) {
        this.name = userRequestDto.getName();
        this.email = userRequestDto.getEmail();
    }

    public boolean isMatchPassword(UserRequestDto dto) {
        return this.password.equals(dto.getPassword());
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