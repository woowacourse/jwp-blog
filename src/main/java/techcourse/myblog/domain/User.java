package techcourse.myblog.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import techcourse.myblog.validation.UserPattern;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

@Entity
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, unique = true, length = 25)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    private LocalDateTime createTimeAt;
    @UpdateTimestamp
    private LocalDateTime updateTimeAt;

    private User() {
    }

    public User(String name, String email, String password) {
        validateName(name);
        validateEmail(email);
        validatePassword(password);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validateName(String name) {
        if (isEmpty(name) || !Pattern.matches(UserPattern.NAME_PATTERN, name)) {
            throw new InvalidUserException(UserPattern.NAME_CONSTRAINT_MESSAGE);
        }
    }

    private void validateEmail(String email) {
        if (isEmpty(email) || !Pattern.matches(UserPattern.EMAIL_PATTERN, email)) {
            throw new InvalidUserException(UserPattern.EMAIL_CONSTRAINT_MESSAGE);
        }
    }

    private void validatePassword(String password) {
        if (isEmpty(password) || !Pattern.matches(UserPattern.PASSWORD_PATTERN, password)) {
            throw new InvalidUserException(UserPattern.PASSWORD_CONSTRAINT_MESSAGE);
        }
    }

    private boolean isEmpty(String text) {
        return (text == null) || ("").equals(text);
    }

    public User modifyName(String name) {
        validateName(name);
        this.name = name;
        return this;
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
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}