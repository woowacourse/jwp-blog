package techcourse.myblog.domain;

import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.regex.Pattern;

@Entity
public class User {
    private static final String NAME_PATTERN = "^[ㄱ-ㅎ가-힣a-zA-Z]{2,10}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,}$";
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;
    private String password;
    @UniqueElements
    private String email;

    public User() {
    }

    public User(String name, String password, String email) {
        this.name = validateName(name);
        this.password = validatePassword(password);
        this.email = email;
    }

    private String validatePassword(String password) {
        if (!Pattern.matches(PASSWORD_PATTERN, password)) {
            throw new UserException();
        }
        return password;
    }

    private String validateName(String name) {
        if (!Pattern.matches(NAME_PATTERN, name)) {
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

    public void update(User user) {
        this.name = user.name;
        this.password = user.password;
        this.email = user.email;
    }
}
