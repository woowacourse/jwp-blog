package techcourse.myblog.domain;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class User {
    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(length = 100)
    private String userName;

    @NonNull
    @Column(length = 100)
    private String email;

    @NonNull
    @Column(length = 100)
    private String password;

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public void update(User user) {
        this.userName = user.userName;
        this.email = user.email;
        this.password = user.password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id.equals(user.id) &&
                userName.equals(user.userName) &&
                email.equals(user.email) &&
                password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, email, password);
    }
}
