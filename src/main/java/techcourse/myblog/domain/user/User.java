package techcourse.myblog.domain.user;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import techcourse.myblog.domain.article.exception.IllegalUserException;
import techcourse.myblog.domain.user.validation.UserPattern;

import javax.persistence.*;
import java.util.regex.Pattern;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(of = {"id"})
@DynamicUpdate
public class User {
    public static final int NAME_LENGTH = 20;
    public static final int EMAIL_LENGTH = 25;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = NAME_LENGTH)
    private String name;

    @Column(nullable = false, unique = true, length = EMAIL_LENGTH)
    private String email;

    @Column(nullable = false)
    private String password;

    public User(String name, String email, String password) {
        validateName(name);
        validateEmail(email);
        validatePassword(password);
        this.name = name;
        this.email = email;
        this.password = password;
    }
    
    public void modifyName(User user) {
        validateName(user.name);
        this.name = user.name;
    }
    
    private void validateName(String name) {
        if (isEmpty(name) || !Pattern.matches(UserPattern.NAME, name)) {
            throw new IllegalUserException(UserPattern.NAME_CONSTRAINT_MESSAGE);
        }
    }

    private void validateEmail(String email) {
        if (isEmpty(email) || !Pattern.matches(UserPattern.EMAIL, email)) {
            throw new IllegalUserException(UserPattern.EMAIL_CONSTRAINT_MESSAGE);
        }
    }

    private void validatePassword(String password) {
        if (isEmpty(password) || !Pattern.matches(UserPattern.PASSWORD, password)) {
            throw new IllegalUserException(UserPattern.PASSWORD_CONSTRAINT_MESSAGE);
        }
    }

    private boolean isEmpty(String text) {
        return (text == null) || ("").equals(text);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}