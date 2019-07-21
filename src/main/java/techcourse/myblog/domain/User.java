package techcourse.myblog.domain;

import techcourse.myblog.dto.UserRequestDto;

import javax.persistence.*;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class User {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[ㄱ-ㅎ가-힣a-zA-Z]{2,10}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}");
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;
    private String password;

    @Column(unique = true)
    private String email;

    protected User() {
    }

    public User(String name, String password, String email) {
        this.name = validateName(name);
        this.password = validatePassword(password);
        this.email = email;
    }

    private String validatePassword(String password) {
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        if (!matcher.find()) {
            throw new UserException();
        }
        return password;
    }

    private String validateName(String name) {
        Matcher matcher = NAME_PATTERN.matcher(name);
        if (!matcher.find()) {
            throw new UserException();
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

    public void updateNameAndEmail(String name, String email) {
        this.name = name;
        this.email = email;
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