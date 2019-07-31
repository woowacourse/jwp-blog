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
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "USER_NAME", nullable = false, length = 30)
    @NonNull
    private String userName;

    @Column(name = "EMAIL", nullable = false, length = 50)
    @NonNull
    private String email;

    @Column(name = "PASSWORD", nullable = false, length = 30)
    @NonNull
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
