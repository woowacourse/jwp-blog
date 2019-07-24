package techcourse.myblog.users;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "user")
public class User {
    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}";
    public static final String NAME_PATTERN = "[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z]{2,10}";
    public static final String EMAIL_NOT_MATCH_MESSAGE = "메일의 양식을 지켜주세요.";
    public static final String NAME_NOT_MATCH_MESSAGE = "이름은 2자이상 10자이하이며, 숫자나 특수문자가 포함될 수 없습니다.";
    public static final String PASSWORD_NOT_MATCH_MESSAGE = "비밀번호는 8자 이상의 소문자,대문자,숫자,특수문자의 조합이여야 합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "메일을 작성해주세요.")
    @Email(message = EMAIL_NOT_MATCH_MESSAGE)
    private String email;

    @Column
    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(regexp = NAME_PATTERN, message = NAME_NOT_MATCH_MESSAGE)
    private String name;

    @Column
    @NotBlank(message = "패스워드를 입력해주세요.")
    @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_NOT_MATCH_MESSAGE)
    private String password;
}
