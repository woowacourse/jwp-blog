package techcourse.myblog.users;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.regex.Pattern;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false)
    private String password;
    @Column(nullable = false, length = 30)
    private String name;

    @Builder
    private User(final String email, final String name, final String password) {
        validateName(name);
        validateEmail(email);
        validatePassword(password);

        this.email = email;
        this.name = name;
        this.password = password;
    }

    private void validateEmail(final String email) {
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern pattern = Pattern.compile(regex);
        if (email == null || !pattern.matcher(email).find()) {
            throw new IllegalArgumentException("메일의 양식을 지켜주세요.");
        }
    }

    private void validateName(final String name) {
        String regex = "[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z]{2,10}";
        Pattern pattern = Pattern.compile(regex);
        if (name == null  || !pattern.matcher(name).find()) {
            throw new IllegalArgumentException("이름은 2자이상 10자이하이며, 숫자나 특수문자가 포함될 수 없습니다.");
        }
    }

    private void validatePassword(final String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}";;
        Pattern pattern = Pattern.compile(regex);
        if (password == null  || !pattern.matcher(password).find()) {
            throw new IllegalArgumentException("비밀번호는 8자 이상의 소문자,대문자,숫자,특수문자의 조합이여야 합니다.");
        }
    }
}
