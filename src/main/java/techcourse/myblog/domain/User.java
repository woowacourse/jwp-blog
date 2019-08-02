package techcourse.myblog.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@EqualsAndHashCode
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Pattern(regexp = "[^ \\-!@#$%^&*(),.?\":{}|<>0-9]{2,10}",
            message = "이름은 2~10자, 숫자나 특수문자가 포함될 수 없습니다.")
    private String name;

    @Column(nullable = false,
            unique = true)
    @Email(message = "이메일 양식을 지켜주세요.")
    private String email;

    @Column(nullable = false)
    @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*(),.?\\\":{}|<>]{8,}",
            message = "비밀번호는 8자 이상, 소문자, 대문자, 숫자, 특수문자의 조합으로 입력하세요.")
    private String password;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean matchPassword(User user) {
        return this.password.equals(user.password);
    }

    public User modifyName(String name) {
        this.name = name;
        return this;
    }
}
