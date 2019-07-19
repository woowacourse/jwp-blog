package techcourse.myblog.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;
import java.util.regex.Pattern;

@Entity
@Getter @EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;

    @Builder
    public User(final String name, final String email, final String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}