package techcourse.myblog.domain.user;

import techcourse.myblog.exception.UserHasNotAuthorityException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 20)
    private String password;

    public User() {
    }

    public User(final String email, final String name, final String password) {
        this.email = Objects.requireNonNull(email);
        this.name = Objects.requireNonNull(name);
        this.password = password;
    }

    public void update(final User userInfo) {
        if (this.email.equals(userInfo.getEmail())) {
            this.name = userInfo.getName();
            return;
        }
        throw new UserHasNotAuthorityException();
    }

    public boolean match(final User user) {
        return this.equals(user);
    }


    public Long getId() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
