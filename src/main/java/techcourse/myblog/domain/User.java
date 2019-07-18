package techcourse.myblog.domain;

import lombok.*;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Pattern(regexp = "^[a-zA-Z가-힣]{2,10}$", message = "2글자 이상, 10글자 이하로 입력하세요. 숫자와 특수문자는 입력할 수 없습니다.")
    private String name;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~`!@#$%\\^&*()-]).{8,}$", message = "소문자, 대문자, 숫자, 특수문자가 조합된 8글자 이상을 입력하세요 ")
    private String password;
    @Email(message = "Wrong Email")
    @Column()
    private String email;
}
