package techcourse.myblog.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import techcourse.myblog.support.exception.IllegalUserException;
import techcourse.myblog.support.validation.pattern.UserPattern;

import javax.persistence.*;
import java.util.regex.Pattern;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(of = {"id"})
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, unique = true, length = 25)
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

    public User modifyName(String name) {
        validateName(name);
        this.name = name;
        return this;
    }
}