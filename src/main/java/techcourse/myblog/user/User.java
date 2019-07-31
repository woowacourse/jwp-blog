package techcourse.myblog.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import techcourse.myblog.date.Date;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
public class User extends Date {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 10)
    @Length(min = 2, max = 10)
    private String userName;

    @Column(nullable = false, unique = true, length = 50)
    @Email
    private String email;

    @Column(nullable = false, length = 30)
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
