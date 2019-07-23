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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    public User() {
    }

    public User(final String email, final String name, final String password) {
        checkNull(email, name, password);
        this.email = email;
        this.name = name;
        this.password = password;
    }

    private void checkNull(final String email, final String name, final String password) {
        if (Objects.isNull(email) || Objects.isNull(name) || Objects.isNull(password)) {
            throw new NullPointerException();
        }
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

    public void update(final String name) {
        if (Objects.isNull(name)) {
            throw new NameToUpdateNotFoundException("수정할 이름이 존재하지 않습니다.");
        }
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(name, user.name) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, password);
    }
}
