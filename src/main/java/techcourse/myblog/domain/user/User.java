package techcourse.myblog.domain.user;

import techcourse.myblog.exception.NameToUpdateNotFoundException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false)
    private String password;

    public User() {
    }

    public User(final String email, final String name, final String password) {
        this.email = Objects.requireNonNull(email);
        this.name = Objects.requireNonNull(name);
        this.password = Objects.requireNonNull(password);
    }

    public void update(final String name) {
        if (Objects.isNull(name)) {
            throw new NameToUpdateNotFoundException("수정할 이름이 존재하지 않습니다.");
        }
        this.name = name;
    }

    public boolean match(final String email) {
        return this.email.equals(email);
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
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
