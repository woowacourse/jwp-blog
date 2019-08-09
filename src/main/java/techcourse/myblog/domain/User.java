package techcourse.myblog.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    @Column(unique = true)
    @Size(max = 30)
    private String email;

    @NotBlank
    @Pattern(regexp = "^([A-Za-z가-힣]{2,10})$")
    @Column(nullable = false)
    @Size(min = 2, max = 10)
    private String name;

    @NotBlank
    @Pattern(regexp = "^([a-zA-Z0-9!@#$%^&*]{8,})$")
    @Column(nullable = false)
    @Size(max = 20)
    private String password;

    @CreationTimestamp
    private LocalDateTime createdTimeAt;
    @UpdateTimestamp
    private LocalDateTime updateTimeAt;

    private User() {
    }

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public void modify(User user) {
        this.name = user.name;
        this.password = user.password;
    }

    public boolean isMatchPassword(String password) {
        return this.password.equals(password);
    }

    public boolean isNotMatchName(String name) {
        return !this.name.equals(name);
    }

    public boolean isNotMatchEmail(String email) {
        return !this.name.equals(email);
    }

    public boolean isMatchEmail(String email) {
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