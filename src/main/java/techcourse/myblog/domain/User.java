package techcourse.myblog.domain;

import javax.persistence.*;
import java.util.Optional;
import java.util.regex.Pattern;

@Entity
public class User {
    private static final Pattern nameValidation = Pattern.compile("[a-zA-Z가-힣]+");
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 10;
    private static final Pattern passwordValidation = Pattern.compile("[a-zA-Z0-9~!@#$%^&*]+");
    private static final int MIN_PASSWORD_LENGTH = 8;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;

    public User() {}

    public User(String name, String email, String password) {
        this.name = validateName(name);
        this.email = validateEmail(email);
        this.password = validatePassword(password);
    }

    public User update(String name, String email) {
        this.name = validateName(name);
        this.email = validateEmail(email);
        return this;
    }

    public String getName() {
        return this.name;
    }

    private String validateName(String input) {
        return Optional.ofNullable(input).filter(x -> (MIN_NAME_LENGTH <= x.length())
                                                    && (x.length() <= MAX_NAME_LENGTH))
                                        .filter(x -> nameValidation.matcher(x).matches())
                                        .orElseThrow(IllegalArgumentException::new);
    }

    public String getEmail() {
        return this.email;
    }

    private String validateEmail(String input) {
        return Optional.ofNullable(input).filter(x -> x.contains("@"))
                                        .filter(x -> x.substring(x.indexOf("@")).contains("."))
                                        .orElseThrow(IllegalArgumentException::new);
    }

    public boolean emailNotChanged(String email) {
        return this.email.equals(email);
    }

    public String getPassword() {
        return this.password;
    }

    private String validatePassword(String input) {
        return Optional.ofNullable(input).filter(x -> MIN_PASSWORD_LENGTH <= x.length())
                                        .filter(x -> passwordValidation.matcher(x).matches())
                                        .orElseThrow(IllegalArgumentException::new);
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}