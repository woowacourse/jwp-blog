package techcourse.myblog.domain;

import techcourse.myblog.application.dto.UserRequestDto;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Pattern(regexp = "^([A-Za-z가-힣]{2,10})$")
    private String name;

    @NotBlank
    @Pattern(regexp = "^([a-zA-Z0-9!@#$%^&*]{8,})$")
    private String password;

    private User() {
    }

    public static class UserBuilder {
        private String email;
        private String name;
        private String password;

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public User(UserBuilder builder) {
        this.email = builder.email;
        this.name = builder.name;
        this.password = builder.password;
    }

    public User(String email, String name, String password) {
        this(null, email, name, password);
    }

    public User(Long id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public void modify(UserRequestDto user) {
        this.name = user.getName();
        this.password = user.getPassword();
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public boolean compareEmail(String email) {
        return this.email.equals(email);
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{id : " + id +
                " email : " + email + "}";
    }

    public Long getId() {
        return id;
    }
}