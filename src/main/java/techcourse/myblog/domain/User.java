package techcourse.myblog.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Entity
public class User {
    @Id
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^([A-Za-z가-힣]{2,10})$")
    private String name;

    @NotBlank
    @Pattern(regexp = "^([a-zA-Z0-9!@#$%^&*]{8,})$")
    private String password;

    private User() {
    }

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
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
        this.name = user.getName();
        this.password = user.getPassword();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}