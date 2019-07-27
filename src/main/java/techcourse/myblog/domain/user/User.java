package techcourse.myblog.domain.user;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.dto.UserDto;

import javax.persistence.*;
import java.util.List;
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

    @OneToMany(mappedBy = "author")
    private List<Article> aritlces;

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
        this.name = UserName.of(name);
        this.email = UserEmail.of(email);
    }

    public boolean isMatchPassword(UserDto dto) {
        return isMatchPassword(dto.getPassword());
    }

    public boolean isMatchPassword(String password) {
        return this.password.equals(UserPassword.of(password));
    }

    public boolean isMatchEmail(String email) {
        return this.email.equals(UserEmail.of(email));
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
