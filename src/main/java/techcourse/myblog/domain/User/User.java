package techcourse.myblog.domain.User;

import techcourse.myblog.dto.UserDto;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Convert(converter = UserNameConverter.class)
    private UserName name;
    @Convert(converter = UserPasswordConverter.class)
    private UserPassword password;

    @Column(unique = true)
    @Convert(converter = UserEmailConverter.class)
    private UserEmail email;

    protected User() {
    }

    public User(String name, String password, String email) {
        this.name = UserName.of(name);
        this.password = UserPassword.of(password);
        this.email = UserEmail.of(email);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getUserName();
    }

    public String getPassword() {
        return password.getPassword();
    }

    public String getEmail() {
        return email.getEmail();
    }

    public void updateNameAndEmail(String name, String email) {
        this.name.update(name);
        this.email.update(email);
    }

    public boolean isMatchPassword(UserDto dto) {
        return isMatchPassword(dto.getPassword());
    }

    public boolean isMatchPassword(String password) {
        return this.password.match(password);
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
