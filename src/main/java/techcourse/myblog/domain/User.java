package techcourse.myblog.domain;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Email
    @Column(unique = true, nullable = true)
    private String email;

    @NotBlank
    @Pattern(regexp = "^([A-Za-z가-힣]{2,10})$")
    @Column(nullable = true)
    private String name;

    @NotBlank
    @Pattern(regexp = "^([a-zA-Z0-9!@#$%^&*]{8,})$")
    @Column(nullable = true)
    private String password;

    private User() {
    }

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void modify(User user) {
        this.password = user.password;
        this.name = user.name;
    }

    public boolean isSamePassword(String password) {
        return this.password.equals(password);
    }

    public boolean isSameEmail(String email) {
        return this.email.equals(email);
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
