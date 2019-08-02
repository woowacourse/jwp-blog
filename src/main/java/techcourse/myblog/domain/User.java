package techcourse.myblog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import techcourse.myblog.application.service.exception.NotExistUserIdException;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@EqualsAndHashCode(of = "id")
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Pattern(regexp = "^([A-Za-z가-힣]{2,10})$")
    private String name;

    @NotBlank
    @Pattern(regexp = "^([a-zA-Z0-9!@#$%^&*]{8,})$")
    private String password;

    private User() {
    }

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public void modify(User user) {
        if (this.equals(user)) {
            this.password = user.password;
            this.name = user.name;
        }
        throw new NotExistUserIdException("해당 유저가 아닙니다.");

    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public boolean checkEmail(String email) {
        return this.email.equals(email);
    }
}
