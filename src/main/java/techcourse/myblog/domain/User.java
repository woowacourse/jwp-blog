package techcourse.myblog.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import techcourse.myblog.exception.UserException;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Email
    private String email;

    @Column(name = "password", length = 20, nullable = false)
    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void updateUser(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
