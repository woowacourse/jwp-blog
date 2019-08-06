package techcourse.myblog.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@EqualsAndHashCode
@RequiredArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NonNull
    @Column(name = "user_name", nullable = false, length = 30)
    private String userName;

    @NonNull
    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @NonNull
    @Column(name = "password", nullable = false, length = 30)
    private String password;

    public User update(User updatedUser) {
        this.userName = updatedUser.getUserName();
        this.email = updatedUser.getEmail();
        this.password = updatedUser.getPassword();

        return this;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
