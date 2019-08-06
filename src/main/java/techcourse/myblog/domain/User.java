package techcourse.myblog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@EntityListeners(value = {AuditingEntityListener.class})
public class User {
    private static final int MAX_NAME_LENGTH = 10;
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z]*$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 30)
    private String name;

    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    private User() {
    }

    public User(String name, String email, String password) {
        validateName(name);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validateName(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("이름은 " + MAX_NAME_LENGTH + "자 미만이어야 합니다");
        }

        Matcher matcher = NAME_PATTERN.matcher(name);
        if (!matcher.find()) {
            throw new IllegalArgumentException("이름은 알파벳만 가능합니다");
        }
    }

    public void changeName(String name) {
        validateName(name);
        this.name = name;
    }

    public boolean matchEmail(User user) {
        return this.email.equals(user.email);
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
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
