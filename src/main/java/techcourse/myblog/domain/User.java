package techcourse.myblog.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import techcourse.myblog.domain.exception.InvalidEmailFormatException;
import techcourse.myblog.domain.exception.InvalidPasswordFormatException;

@Slf4j
@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,
            unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public User() {
    }

    public User(String name, String email, String password) {
        try {
            validEmailFormat(email);
            validPasswordFormat(password);
            this.name = name;
            this.email = email;
            this.password = password;
        } catch (RuntimeException e) {
            log.error("User Validation Error", e);
        }
    }

    private void validEmailFormat(String email) {
        if (!email.contains("@")) {
            throw new InvalidEmailFormatException("이메일 양식을 지켜주세요.");
        }
    }

    private void validPasswordFormat(String password) {
        if (!password.matches("[a-zA-Z0-9!@#$%^&*(),.?\\\":{}|<>]{8,}")) {
            throw new InvalidPasswordFormatException("비밀번호는 8자 이상, 소문자, 대문자, 숫자, 특수문자의 조합으로 입력하세요.");
        }
    }

    public boolean matchPassword(User user) {
        return this.password.equals(user.password);
    }

    public boolean matchEmail(User user) {
        return this.email.equals(user.email);
    }

    public void modifyName(String name) {
        this.name = name;
    }
}
