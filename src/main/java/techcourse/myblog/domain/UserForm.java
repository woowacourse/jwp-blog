package techcourse.myblog.domain;

import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
public class UserForm {
    private long id;
    @Pattern(regexp = "^[a-zA-Z가-힣]{2,10}$", message = "2글자 이상, 10글자 이하로 입력하세요. 숫자와 특수문자는 입력할 수 없습니다.")
    private String name;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~`!@#$%\\^&*()-]).{8,}$", message = "소문자, 대문자, 숫자, 특수문자가 조합된 8글자 이상을 입력하세요 ")
    @ColumnTransformer(write = "SHA2(?,224)")
    private String password;
    @Email(message = "Wrong Email")
    @Column(unique=true)
    private String email;

    public User toUser(PasswordEncoder passwordEncoder) {
        String encryptedPassword = passwordEncoder.encode(password);
        if (id != 0) {
            return new User(id, name, encryptedPassword, email);
        }
        return new User(name, encryptedPassword, email);
    }
}
