package techcourse.myblog.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = {"id"})
@ToString
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "email", name = "uniqueEmailConstraint")}
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false)
    private String email;

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public void update(User user) {
        this.name = user.getName();
        this.password = user.getPassword();
        this.email = user.getEmail();
    }

    public boolean isMatch(User user) {
        return email.equals(user.getEmail())
                && password.equals(user.getPassword());
    }
}
