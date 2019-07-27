package techcourse.myblog.users;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table(uniqueConstraints = @UniqueConstraint(name = "email", columnNames = {"email"}))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Builder
    private User(final Long id, final String email, final String name, final String password) {
        if (id == null) {
            UserValidator.validateEmail(email);
            UserValidator.validatePassword(password);
        }
        UserValidator.validateName(name);

        this.email = email;
        this.name = name;
        this.password = password;
    }

    public boolean authenticate(final String password) {
        return this.password.equals(password);
    }

    void update(final User other) {
        this.name = other.name;
    }
}

