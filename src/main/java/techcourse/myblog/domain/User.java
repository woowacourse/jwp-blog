package techcourse.myblog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import techcourse.myblog.domain.exception.InvalidUserException;
import techcourse.myblog.validation.UserPattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.regex.Pattern;

@Entity
@DynamicUpdate
@EqualsAndHashCode(of = "email", callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Getter
public class User extends BaseEntity {
    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, unique = true, length = 25)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
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
        if (isEmpty(name) || !Pattern.matches(UserPattern.NAME_PATTERN, name)) {
            throw new InvalidUserException(UserPattern.NAME_CONSTRAINT_MESSAGE);
        }
    }

    private void validateEmail(String email) {
        if (isEmpty(email) || !Pattern.matches(UserPattern.EMAIL_PATTERN, email)) {
            throw new InvalidUserException(UserPattern.EMAIL_CONSTRAINT_MESSAGE);
        }
    }

    private void validatePassword(String password) {
        if (isEmpty(password) || !Pattern.matches(UserPattern.PASSWORD_PATTERN, password)) {
            throw new InvalidUserException(UserPattern.PASSWORD_CONSTRAINT_MESSAGE);
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

    public boolean matchId(Long userId) {
        return getId().equals(userId);
    }
}