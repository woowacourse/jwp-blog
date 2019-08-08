package techcourse.myblog.domain;

import org.hibernate.validator.constraints.Length;
import techcourse.myblog.domain.base.BaseEntity;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.exception.InvalidNameException;
import techcourse.myblog.exception.InvalidPasswordException;

import javax.persistence.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class User extends BaseEntity {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[ㄱ-ㅎ가-힣a-zA-Z]{2,10}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}");

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
            throw new InvalidPasswordException();
        }
        return password;
    }

    private String validateName(String name) {
        Matcher matcher = NAME_PATTERN.matcher(name);
        if (!matcher.find()) {
            throw new InvalidNameException();
        }
        return name;
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
}