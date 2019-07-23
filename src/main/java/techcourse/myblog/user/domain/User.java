package techcourse.myblog.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;

    private String password;

    private String name;

    @Builder
    private User(long id, String email, String password, String name) {
        this.id = id;
        this.email = validateEmail(email);
        this.password = validatePassword(password);
        this.name = validateName(name);
    }

    public void update(String name) {
        this.name = validateName(name);
    }

    private String validateEmail(final String email) {
        String emailRegex = "^[_a-zA-Z0-9-.]+@[.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("잘못된 형식의 이메일입니다.");
        }
        return email;
    }

    private String validatePassword(final String password) {
        String passwordRegex = ".*(?=^.{8,}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*";
        if (!password.matches(passwordRegex)) {
            throw new IllegalArgumentException("잘못된 형식의 비밀번호입니다.");
        }
        return password;
    }

    private String validateName(final String name) {
        String nameRegex = "[A-Za-zㄱ-ㅎㅏ-ㅣ가-힣]{2,10}";
        if (!name.matches(nameRegex)) {
            throw new IllegalArgumentException("이름은 2자 이상 10자 이하입니다.");
        }
        return name;
    }
}
