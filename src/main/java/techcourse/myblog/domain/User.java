package techcourse.myblog.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(name = "email", columnNames = {"email"}))
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private String password;

    @Builder
    public User(final Long id, final String email, final String name, final String password) {
        if (id == null) {
            UserValidator.validateEmail(email);
            UserValidator.validatePassword(password);
        }
        UserValidator.validateName(name);
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public boolean authenticate(final String password) {
        return this.password.equals(password);
    }

    public void update(final User other) {
        this.name = other.name;
    }

}

