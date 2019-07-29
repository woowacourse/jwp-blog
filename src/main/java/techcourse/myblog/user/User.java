package techcourse.myblog.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 6)
    @Length(min = 2, max = 6)
    private String userName;

    @Column(nullable = false, unique = true, length = 25)
    @Email
    private String email;

    @Column(nullable = false, length = 10)
    @Length(min = 8)
    private String password;

    @Builder
    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public void changeUserName(String userName) {
        this.userName = userName;
    }
}
